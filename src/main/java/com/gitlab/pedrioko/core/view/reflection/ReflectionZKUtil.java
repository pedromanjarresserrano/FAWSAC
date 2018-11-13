package com.gitlab.pedrioko.core.view.reflection;

import com.gitlab.pedrioko.core.lang.annotation.Reference;
import com.gitlab.pedrioko.core.view.api.ValidateAnnotation;
import com.gitlab.pedrioko.core.view.api.impl.PasswordTransformerAnnotation;
import com.gitlab.pedrioko.core.view.reflection.enums.ClassMethod;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.zk.component.ChosenBox;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionZKUtil {

    private static final String ERROR_ON_GET_BINDING_VALUE = "ERROR on getBindingValue()";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionZKUtil.class);

    public static boolean isInEventListener() {
        return Executions.getCurrent() != null;
    }

    public static Object getValueComponent(Component component) {

        if (component != null)
            try {
                Class<? extends Component> class1 = component.getClass();

                if (class1 == Combobox.class) {
                    Method method = component.getClass().getMethod("getSelectedItem");
                    Object invoke = method.invoke(component);
                    return getValueComponent((Component) invoke);
                } else if (class1 == ChosenBox.class) {
                    Method method = component.getClass().getMethod("getValueSelection");
                    Object invoke = method.invoke(component);
                    if (invoke instanceof ArrayList)
                        return new ArrayList<>((ArrayList) invoke);
                    else
                        return new ArrayList<>((PersistentBag) invoke);
                } else if (class1 == Checkbox.class) {
                    return isChecked(component);
                } else {
                    if (class1 == Gmaps.class)
                        return null;
                    else {
                        return getValue(component);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("Error on getValueComponent()", e);
                return null;
            }
        return null;

    }

    public static Object getBindingValue(Map<Field, Component> binding, Class<?> klass, Object newInstance) {
        binding.forEach((k, v) -> {
            if (k.isAnnotationPresent(Reference.class)) {
                String methodName = ReflectionJavaUtil.getNameMethod(k, ClassMethod.SET);
                List valueComponent = (List) getValueComponent(v);
                Object collect = valueComponent.stream().map(e -> ReflectionJavaUtil.getIdValue(e)).filter(Objects::nonNull).map(Object::toString).collect(Collectors.toList());
                try {
                    Method methodSetProperty = klass.getMethod(methodName, k.getType());
                    methodSetProperty.invoke(newInstance, collect);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    LOGGER.error(ERROR_ON_GET_BINDING_VALUE, e);
                }
            } else if (v.getClass() != Gmaps.class) {
                String methodName = ReflectionJavaUtil.getNameMethod(k, ClassMethod.SET);
                Object valueComponent = getValueComponent(v);
                ApplicationContextUtils.getBeans(ValidateAnnotation.class).forEach(e -> e.Validate(k, valueComponent));
                try {
                    Object aux = ApplicationContextUtils.getBean(PasswordTransformerAnnotation.class).Validate(k, valueComponent);
                    Method methodSetProperty = klass.getMethod(methodName, k.getType());
                    methodSetProperty.invoke(newInstance, k.getType() == Set.class ? new HashSet((Collection) aux) : aux);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    LOGGER.error(ERROR_ON_GET_BINDING_VALUE, e);
                }

            }

        });
        return newInstance;

    }

    public static Object getBindingValue(Component c, String fieldname, Class<?> klass, Object newInstance) {
        try {
            String methodName = ReflectionJavaUtil.getNameMethod(fieldname, ClassMethod.SET);
            Object valueComponent = getValueComponent(c);
            Field field = klass.getField(fieldname);
            ApplicationContextUtils.getBeans(ValidateAnnotation.class).forEach(e -> e.Validate(field, valueComponent));
            Method methodSetProperty = klass.getMethod(methodName, field.getType());
            methodSetProperty.invoke(newInstance, valueComponent);
            return newInstance;
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException
                | NoSuchFieldException e) {
            LOGGER.error(ERROR_ON_GET_BINDING_VALUE, e);
            return null;
        }

    }

    public static void setValueComponent(Component component, Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<? extends Component> class1 = component.getClass();
        if (class1 == Combobox.class) {
            Method method = component.getClass().getMethod("getItems");
            Object invoke = method.invoke(component);
            List<Comboitem> list = (List<Comboitem>) invoke;
            Object idvalue = ReflectionJavaUtil.getIdValue(obj);
            list.stream().filter(e -> {
                Object idValue = ReflectionJavaUtil.getIdValue(e.getValue());
                boolean b = idvalue != null && idvalue.equals(idValue);
                Object eValue = e.getValue();
                boolean b1 = eValue.equals(obj);
                return (b1 && idValue == null) || b;
            }).forEach(e -> {
                try {
                    setSelectedItem(component, e);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
                    LOGGER.error("ERROR on setValueComponent()", e1);
                }
            });

        } else if (class1 == Checkbox.class) {
            Checkbox c = (Checkbox) component;
            c.setChecked((Boolean) obj);
        } else if (class1 == ChosenBox.class) {
            ChosenBox c = (ChosenBox) component;
            c.setValueSelection((List) obj);
        } else if (obj != null && obj.getClass() == PersistentBag.class) {
            setValue(component, new ArrayList((PersistentBag) obj));
        } else if (obj != null && obj.getClass() == PersistentSet.class) {
            setValue(component, new ArrayList((PersistentSet) obj));
        } else {
            if (class1 != Gmaps.class) {
                setValue(component, obj);
            }
        }

    }

    public static void disableBinding(Map<Field, Component> binding) {
        binding.forEach((k, v) -> {
            try {
                if (v.getClass() != Gmaps.class)
                    disableComponent(v);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("ERROR on disableBinding()", e);
            }
        });
    }

    public static void readOnlyBinding(Map<Field, Component> binding) {
        binding.forEach((k, v) -> {
            try {
                if (v.getClass() != Gmaps.class)
                    readOnlyComponent(v);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("ERROR on disableBinding()", e);
            }
        });
    }

    public static Object getValue(Component component)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = component.getClass().getMethod("getValue");
        return method.invoke(component);
    }

    public static <T> void setValue(Component component, T object)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (object != null) {
            Method method = component.getClass().getMethod("setValue", object.getClass());
            method.invoke(component, object);
        }
    }

    public static Object isChecked(Component component)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = component.getClass().getMethod("isChecked");
        return method.invoke(component);

    }

    public static <T> void sethecked(Component component, T object)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (object != null) {
            Method method = component.getClass().getMethod("setChecked", object.getClass());
            method.invoke(component, object);
        }
    }

    public static void widthComponent(Component component) {
        try {
            Method method = component.getClass().getMethod("setWidth", String.class);
            method.invoke(component, "90%");
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            LOGGER.error("ERROR on widthComponent()", e);
        }
    }

    public static void disableComponent(Component component)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = component.getClass().getMethod("setDisabled", boolean.class);
        method.invoke(component, true);
    }

    public static void readOnlyComponent(Component component)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = component.getClass().getMethod("setReadonly", boolean.class);
        method.invoke(component, true);
    }


    public static <T> void setSelectedItem(Component component, T object)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (object != null) {
            Method method = component.getClass().getMethod("setSelectedItem", object.getClass());
            method.invoke(component, object);
        }
    }

    public static <T> void populate(Combobox combobox, List<T> list, boolean autolabel) {
        for (Object w : list) {
            Comboitem comboitem = new Comboitem();
            String label = w.toString();
            comboitem.setLabel(autolabel ? ReflectionZKUtil.getLabel(label) : label);
            comboitem.setValue(w);
            combobox.getItems().add(comboitem);
        }
    }

    public static String getLabel(Field e) {
        String name = e.getName();
        return getLabel(name);
    }

    public static String getLabel(String name) {
        String label = Labels.getLabel(name);
        return label == null || label.isEmpty() ? StringUtil.getCapitalize(name) : StringUtil.getCapitalize(label);
    }


}

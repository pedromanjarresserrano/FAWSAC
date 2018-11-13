package com.gitlab.pedrioko.core.view.forms;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.view.action.api.Action;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.enums.AplicateAllClass;
import com.gitlab.pedrioko.core.view.enums.CrudAction;
import com.gitlab.pedrioko.core.view.enums.SubCrudView;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.StringUtil;
import com.gitlab.pedrioko.core.view.viewers.CrudView;
import com.gitlab.pedrioko.core.zk.component.ChosenBox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class AccessForm extends CustomForm {

    private static final long serialVersionUID = 1L;
    private final transient List<Action> beans;
    private final ChosenBox actions;
    private final Combobox menuprovider;

    public AccessForm() {
        super(ProviderAccess.class, new LinkedHashMap<String, Class<?>>() {
            private static final long serialVersionUID = 1L;

            {
                put("menuprovider", Combobox.class);
                put("actions", ChosenBox.class);
            }
        });
        beans = ApplicationContextUtils.getBeans(Action.class);
        menuprovider = (Combobox) getComponentOfField("menuprovider");
        ApplicationContextUtils.getBeans(MenuProvider.class).stream().map(MenuProvider::getClass)
                .map(Class::getSimpleName).forEach(e -> {
            Comboitem comboitem = new Comboitem();
            comboitem.setLabel(e);
            comboitem.setValue(e);
            menuprovider.getItems().add(comboitem);
        });
        actions = (ChosenBox) getComponentOfField("actions");
        menuprovider.addEventListener(Events.ON_CHANGE, e -> loadChosenbox());
    }

    public void loadChosenbox() {
        List<String> listmodel = new ArrayList<>();
        String string = menuprovider.getSelectedItem().getValue();
        loadActions(listmodel, string);

        actions.setModel((listmodel));
    }

    private void loadActions(List<String> listmodel, String string) {
        addToList(listmodel, null);
        addToList(listmodel, AplicateAllClass.class);
        addToList(listmodel, SubCrudView.class);
        Object bean = getBean(StringUtil.getDescapitalize(string));
        addToList(listmodel, bean.getClass());
        if (bean instanceof MenuProvider) {
            Component view = ((MenuProvider) bean).getView();
            if (view instanceof CrudView) {
                addToList(listmodel, ((CrudView) view).getTypeClass());
            }

        }
    }

    private Object getBean(String arg0) {
        return ApplicationContextUtils.getBean(arg0);
    }

    private void addToList(List listmodel, Class<?> klass) {
        beans.stream().filter(x -> x.getAplicateClass() != null)
                .filter(x -> x.getAplicateClass().contains(klass)).map(Action::getClass)
                .map(Class::getSimpleName).forEach(listmodel::add);
        if (klass == null) {
            beans.stream().filter(x -> x.getAplicateClass().contains(CrudAction.class) || x.getAplicateClass().contains(AplicateAllClass.class)).map(Action::getClass).map(Class::getSimpleName)
                    .forEach(listmodel::add);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.core.view.forms.Form#setValueForm(java.lang.
     * Object)
     */
    @Override
    public void setValueForm(Object obj) {
        super.setValueForm((ProviderAccess) obj);
        ProviderAccess pva = (ProviderAccess) obj;
        if (menuprovider.getSelectedItem() != null) {
            loadChosenbox();
            actions.setValueSelection(pva.getActions());
        }
    }

}

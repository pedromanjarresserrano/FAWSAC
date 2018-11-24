package com.gitlab.pedrioko.core.view.controllers;

import com.gitlab.pedrioko.core.lang.DateRange;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.api.OnQuery;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.enums.ParamMode;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CollectionPath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.PathBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

public class CrudController {

    private static final long serialVersionUID = 1L;
    private final Class<?> klass;
    private final transient CrudService crudService;
    private final transient Map<String, Object> map = new HashMap<>();
    private final transient Map<String, Object> paramsroot = new HashMap<>();
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>();
    private final List<OnQuery> postEvent = new ArrayList<>();
    private List values;
    private @Getter
    @Setter
    ParamMode paramMode = ParamMode.AND;
    private @Getter
    @Setter
    Boolean reloadable;


    public CrudController(Class<?> klass, List<?> values) {
        super();
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        this.klass = klass;
        this.values = values;
    }

    private Class<?> getTypeClass() {
        return klass;
    }

    public void addValue(Object value) {
        if (!values.contains(value)) {
            ((ArrayList) values).add(value);
            this.onEvent.get(CrudEvents.ON_ADD).forEach(OnEvent::doSomething);
        } else
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("onlist"), MessageType.WARNING);
    }

    public void setValue(List value) {
        values.clear();
        values.addAll(value);
        postEvent.forEach(OnQuery::doSomething);
    }

    public void setValue(ArrayList value) {
        values.clear();
        values.addAll(value);
        postEvent.forEach(OnQuery::doSomething);
    }

    public List<String> getValueIds() {
        return (List<String>) values.stream().map(ReflectionJavaUtil::getIdValue).map(Object::toString).collect(Collectors.toList());
    }

    private void setValueIds(Collection<Long> value) {
        CrudService crudService = getBean(CrudService.class);
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(getTypeClass());
        String idPropertyName = crudService.getIdPropertyName(klass);
        PathBuilder<Object> t = pathBuilder.get(idPropertyName);
        List fetch = crudService.query().from(pathBuilder).where(t.in((List) value)).fetch();
        setValue((ArrayList<?>) fetch);
    }

    public void setValueIds(List<String> value) {
        setValueIds(value.stream().map(Long::parseLong).collect(Collectors.toList()));
    }

    public Object getRoot(Object key) {
        return paramsroot.get(key);
    }

    public Object putRoot(String key, Object value) {
        Object put = paramsroot.put(key, value);
        doQuery();
        return put;
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object put(String key, Object value) {
        Object put = map.put(key, value);
        doQuery();
        return put;
    }

    public void clearParams() {
        map.clear();
    }

    public void doQuery() {
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(klass);
        Predicate where = null;
        map.putAll(paramsroot);
        for (Map.Entry<String, Object> v : map.entrySet()) {
            Object value = v.getValue();
            switch (paramMode) {
                case AND:
                    if (value instanceof DateRange) {
                        DatePath<Date> date = pathBuilder.getDate(v.getKey(), Date.class);
                        where = where != null ? date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin()).and(where) : date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin());
                    } else {
                        CollectionPath collection = pathBuilder.getCollection(v.getKey(), value.getClass());
                        if (collection != null) {
                            if (!(value instanceof Collection))
                                where = where != null ? collection.contains(value).and(where) : collection.contains(value);
                            else {
                                for (Object val : (Collection) value) {
                                    where = where != null ? collection.contains(val).and(where) : collection.contains(val);
                                }
                            }
                        } else
                            where = where != null ? pathBuilder.get(v.getKey()).eq(value).and(where) : pathBuilder.get(v.getKey()).eq(value);
                    }
                    break;
                case OR:
                    if (value instanceof DateRange) {
                        DatePath<Date> date = pathBuilder.getDate(v.getKey(), Date.class);
                        where = where != null ? date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin()).or(where) : date.between(((DateRange) value).getInicio(), ((DateRange) value).getFin());
                    } else {
                        CollectionPath collection = pathBuilder.getCollection(v.getKey(), value.getClass());
                        if (collection != null) {
                            if (!(value instanceof Collection))
                                where = where != null ? collection.contains(value).or(where) : collection.contains(value);
                            else
                                for (Object val : (Collection) value) {
                                    where = where != null ? collection.contains(val).or(where) : collection.contains(val);
                                }
                        } else
                            where = where != null ? pathBuilder.get(v.getKey()).eq(value).or(where) : pathBuilder.get(v.getKey()).eq(value);
                    }
                    break;
            }
        }

        setValue(map.isEmpty() ? (ArrayList) crudService.getAll(klass) : (ArrayList) crudService.query().from(pathBuilder).where(where).fetch());

    }

    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = this.onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        this.onEvent.put(e, onEvents);
    }


    public void addEventPostQuery(OnQuery o) {
        this.postEvent.add(o);

    }
}

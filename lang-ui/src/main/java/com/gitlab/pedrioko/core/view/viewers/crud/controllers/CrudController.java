package com.gitlab.pedrioko.core.view.viewers.crud.controllers;

import com.gitlab.pedrioko.core.lang.annotation.CrudOrderBy;
import com.gitlab.pedrioko.core.lang.api.RangeValue;
import com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.api.OnQuery;
import com.gitlab.pedrioko.core.view.enums.CrudEvents;
import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.enums.ParamMode;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.model.OrderBY;
import com.gitlab.pedrioko.core.view.viewers.crud.grid.AlphabetFilter;
import com.gitlab.pedrioko.services.CrudService;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.BindUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.gitlab.pedrioko.core.reflection.ReflectionJavaUtil.getCollectionsFields;
import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CrudController {

    private static final long serialVersionUID = 1L;
    private final Class<?> klass;
    private final  CrudService crudService;
    private final  Map<String, Object> map = new HashMap<>();
    private final  Map<String, Object> paramsroot = new HashMap<>();
    private final Map<CrudEvents, List<OnEvent>> onEvent = new HashMap<>();
    private final List<OnQuery> postEvent = new ArrayList<>();
    private @Getter
    @Setter
    String beginString = "";
    private List values;
    private @Getter
    @Setter
    ParamMode paramMode = ParamMode.AND;
    private @Getter
    @Setter
    Boolean reloadable;
    private int offSet = 0;
    private int limit = 16;
    private Boolean crudViewValue = FALSE;
    private List crudViewValueList;
    private String orderField;
    private OrderBY orderBY = OrderBY.ASC;


    public CrudController(Class<?> klass) {
        super();
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        this.klass = klass;
        values = new ArrayList();
    }

    public CrudController(Class<?> klass, List<?> values) {
        super();
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        this.klass = klass;
        this.values = values;
    }

    public Class<?> getTypeClass() {
        return klass;
    }

    public void addValue(Object value) {
        if (!values.contains(value)) {
            ((ArrayList) values).add(value);
            if (crudViewValue)
                crudViewValueList.add(value);
            List<OnEvent> onEvents = onEvent.get(CrudEvents.ON_ADD);
            if (onEvents != null) onEvents.forEach(OnEvent::doSomething);
        } else
            ZKUtil.showMessage(ReflectionZKUtil.getLabel("onlist"), MessageType.WARNING);
    }

    public void setValue(List value) {
        values.clear();
        values.addAll(value);
        postEvent.forEach(OnQuery::doSomething);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
    }

    public void setValue(ArrayList value) {
        setValue((List) value);
    }

    public void setCrudViewValue(List<?> value) {
        crudViewValue = TRUE;
        crudViewValueList = value != null ? value : new ArrayList();
        doQuery();
    }

    public List<String> getValueIds() {
        return (List<String>) values.stream().map(ReflectionJavaUtil::getIdValue).map(Object::toString).collect(Collectors.toList());
    }


    public void setValueIds(List<String> value) {
        value = (List) value.stream().map(Long::parseLong).collect(Collectors.toList());
        CrudService crudService = getBean(CrudService.class);
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(getTypeClass());
        String idPropertyName = crudService.getIdPropertyName(klass);
        PathBuilder<Object> t = pathBuilder.get(idPropertyName);
        List fetch = crudService.query().from(pathBuilder).where(t.in(value)).fetchAll().fetch();
        setValue((ArrayList<?>) fetch);
    }

    public Object getRoot(Object key) {
        return paramsroot.get(key);
    }

    public Object putRoot(String key, Object value) {
        return paramsroot.put(key, value);
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object put(String key, Object value) {
        return map.put(key, value);
    }


    public void clearParams() {
        map.clear();
        beginString = "";
    }

    public void doQuery() {
        if (crudViewValue) {
            if (crudViewValueList.size() > limit)
                setValue(crudViewValueList.subList(offSet, offSet + limit));
            else
                setValue(crudViewValueList);
        } else {
            PathBuilder pathBuilder = crudService.getPathBuilder(klass);
            Predicate where = getPredicate(pathBuilder);
            JPAQuery<?> jpaQuery = crudService.query().from(pathBuilder).offset(offSet).limit(limit);
            OrderSpecifier orderBy = getOrderByField(pathBuilder);
            if (orderBy != null)
                jpaQuery.orderBy(orderBy);
            if (where != null)
                jpaQuery = jpaQuery.where(where);
            loadInnners(jpaQuery, pathBuilder);
            if (klass.isAnnotationPresent(CrudOrderBy.class) && (orderField == null || orderField.isEmpty())) {
                String value = klass.getAnnotation(CrudOrderBy.class).value();
                if (value != null) {
                    Field field = ReflectionJavaUtil.getField(klass, value);
                    OrderSpecifier asc = pathBuilder.getComparable(value, field.getType()).asc();
                    jpaQuery = jpaQuery.orderBy(asc);
                }
            }
            setValue((ArrayList) jpaQuery.fetch());

        }
    }

    private JPAQuery<?> loadInnners(JPAQuery<?> jpaQuery, PathBuilder pathBuilder) {
        List<Field> collectionsFields = getCollectionsFields(getTypeClass());
        collectionsFields.forEach(e -> {
            PathBuilder alias = pathBuilder.get(e.getName());
            jpaQuery.leftJoin(alias);
        });
        if (!collectionsFields.isEmpty()) jpaQuery.fetchJoin();
        return jpaQuery;
    }

    private OrderSpecifier getOrderByField(PathBuilder pathBuilder) {
        if (orderField != null && !orderField.isEmpty()) {
            ComparablePath cp = pathBuilder.getComparable(orderField, ReflectionJavaUtil.getField(this.getTypeClass(), orderField).getType());
            OrderSpecifier orderSpecifier = null;
            switch (orderBY) {
                case ASC:
                    orderSpecifier = cp.asc();
                    break;
                case DESC:
                    orderSpecifier = cp.desc();
                    break;
            }
            return orderSpecifier;
        }
        return null;
    }

    private Predicate getPredicate(PathBuilder pathBuilder) {
        Predicate where = null;
        //map.putAll(paramsroot);
        where = getPredicate(map, pathBuilder, where);
        where = getPredicate(paramsroot, pathBuilder, where);
        return beginString != null && !beginString.isEmpty() ? crudService.getLikePredicate(beginString.trim(), ReflectionJavaUtil.getStringFields(klass), pathBuilder, where) : where;
    }

    private Predicate getPredicate(Map<String, Object> map, PathBuilder pathBuilder, Predicate where) {
        for (Map.Entry<String, Object> v : map.entrySet()) {
            Object value = v.getValue();
            if (value instanceof String && ((String) value).startsWith(AlphabetFilter.STARTWITH)) {
                StringPath stringPath = pathBuilder.getString(v.getKey());
                String substring = ((String) value).substring(AlphabetFilter.STARTWITH.length());
                String lowerCase = substring.toLowerCase();
                BooleanExpression startsWith = stringPath.startsWith(substring);
                BooleanExpression rightExpression = stringPath.startsWith(lowerCase);
                where = where != null ? paramMode == ParamMode.AND ? startsWith.or(rightExpression).and(where) : stringPath.startsWith(((String) value).substring(AlphabetFilter.STARTWITH.length() - 1)).
                        or(rightExpression).or(where) :
                        startsWith.or(rightExpression);
                continue;
            }
            if (value instanceof RangeValue) {
                ComparablePath date = pathBuilder.getComparable(v.getKey(), ((RangeValue) value).getInicio().getClass());
                BooleanExpression betweenExpression = date.between(((RangeValue) value).getInicio(), (Comparable) ((RangeValue) value).getFin());
                where = where != null ? paramMode == ParamMode.AND ? betweenExpression.and(where) : betweenExpression.or(where) : betweenExpression;
                continue;
            }
            if (value instanceof Collection) {
                Class type = pathBuilder.getType();
                Field field = ReflectionJavaUtil.getField(type, v.getKey());
                Class aClass = field.getType();
                boolean assignableFrom = Arrays.asList(aClass.getInterfaces()).contains(Collection.class);
                if (assignableFrom) {
                    CollectionPath collection = pathBuilder.getCollection(v.getKey(), value.getClass());
                    if (collection != null) {
                        if (!(value instanceof Collection)) {
                            BooleanExpression contains = collection.contains(value);
                            where = where != null ? paramMode == ParamMode.AND ? contains.and(where) : contains.or(where) : contains;
                        } else
                            for (Object val : (Collection) value) {
                                BooleanExpression expression = (val == null) ? collection.isEmpty() : collection.contains(val);
                                where = where != null ? paramMode == ParamMode.AND ? expression.and(where) : expression.or(where) : expression;
                            }
                    }
                } else {
                    where = where != null ? paramMode == ParamMode.AND ? pathBuilder.get(v.getKey()).in((Collection) value).and(where) : pathBuilder.get(v.getKey()).in((Collection) value).or(where) : pathBuilder.get(v.getKey()).in((Collection) value);
                    //            for (Object val : (Collection) value) {
                    //        where = where != null ? pathBuilder.get(v.getKey()).eq(val).or(where) : pathBuilder.get(v.getKey()).eq(val);
                    //      }
                }
                continue;
            }
            BooleanExpression expression = pathBuilder.get(v.getKey()).eq(value);
            where = where != null ? paramMode == ParamMode.AND ? expression.and(where) : expression.or(where) : expression;


        }
        return where;
    }

    public void addEventOnEvent(CrudEvents e, OnEvent o) {
        List<OnEvent> onEvents = onEvent.get(e);
        if (onEvents == null) {
            onEvents = new ArrayList<>();
        }
        onEvents.add(o);
        onEvent.put(e, onEvents);
    }


    public void addEventPostQuery(OnQuery o) {
        postEvent.add(o);

    }

    public void setContainsString(String beginString) {
        this.beginString = beginString;
        this.offSet = 0;
        doQuery();
    }

    public void doQueryStringBegin(String field, String value) {
        if (value.isEmpty() || value.compareToIgnoreCase(AlphabetFilter.STARTWITH + "ALL") == 0) {
            map.remove(field);
        } else {
            map.put(field, value);
        }
        this.offSet = 0;
        doQuery();

    }

    public void setPage(int offSet) {
        this.offSet = offSet;
        setPage(offSet, limit);
    }

    public void setPage(int offSet, int limit) {
        if (limit == 0)
            throw new IllegalArgumentException("Limit can't be 0");
        this.offSet = offSet;
        this.limit = limit;
        List<OnEvent> onEvents = onEvent.get(CrudEvents.ON_SET_PAGE_SIZE);
        if (onEvents != null) onEvents.forEach(OnEvent::doSomething);
        doQuery();


    }

    public void setPageSize(int limit) {
        setPage(offSet, limit);
    }


    public long getCount() {
        if (crudViewValue) {
            return crudViewValueList != null ? crudViewValueList.size() : 0;
        } else {
            //  map.putAll(paramsroot);
            PathBuilder<?> pathBuilder = crudService.getPathBuilder(klass);
            JPAQuery<?> query = crudService.query().from(pathBuilder);
            Predicate predicate = getPredicate(pathBuilder);
            JPAQuery<?> where = query.where(predicate);
            return where.fetch().size();
        }
    }

    public List getValues() {
        if (crudViewValue) {
            values.clear();
            values.addAll(this.crudViewValueList);
        }
        return values;

    }

    public int getOffSet() {
        return offSet;
    }

    public int getPage() {
        return offSet / limit;
    }

    public int getLimit() {
        return limit;
    }

    public Object getFullData() {
        if (crudViewValue) {
            return this.crudViewValueList;
        } else {
            // map.putAll(paramsroot);
            PathBuilder<?> pathBuilder = crudService.getPathBuilder(klass);
            JPAQuery<?> query = crudService.query().from(pathBuilder).where(getPredicate(pathBuilder));
            loadInnners(query, pathBuilder);
            return query.fetch();
        }
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public OrderBY getOrderBY() {
        return orderBY;
    }

    public void setOrderBY(OrderBY orderBY) {
        this.orderBY = orderBY;
    }

    public void putAllRoot(Map<? extends String, ?> m) {
        paramsroot.putAll(m);
    }
}

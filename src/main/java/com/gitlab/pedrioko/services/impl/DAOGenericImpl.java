package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.hibernate.MySQLJPATemplates;
import com.gitlab.pedrioko.core.lang.annotation.CrudOrderBy;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.services.DAOGeneric;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;

import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The Class GenericJPAImpl.
 *
 * @param <T> the generic type
 */
@Repository
public class DAOGenericImpl<T> implements DAOGeneric {
    private static final Logger LOGGER = LoggerFactory.getLogger(DAOGenericImpl.class);


    @PersistenceContext
    private EntityManager entityManager;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#delete(java.lang.Object)
     */
    @Override
    public <T> void delete(T klass) {
        entityManager.remove(entityManager.merge(klass));
    }

    @Override
    public <T> T refresh(T klass) {
        if (klass != null)
            entityManager.refresh(klass);
        return klass;
    }

    @Override
    public JPAQuery<?> query() {
        return new JPAQuery<>(entityManager);
    }

    @Override
    public JPAQuery<?> queryRand() {
        return new JPAQuery<>(entityManager, MySQLJPATemplates.DEFAULT);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#getAll(java.lang.Class)
     */
    @Override
    public <T> List<T> getAll(Class<T> klass) {
        PathBuilder<?> path = getPathBuilder(klass);
        return (List<T>) query().from(path).fetch();
    }

    @Override
    public <T> List<T> getAllOrder(Class<T> klass) {
        PathBuilder pathBuilder = getPathBuilder(klass);
        String orderBy = getOrderBy(klass);
        if (orderBy != null && !orderBy.isEmpty()) {
            OrderSpecifier asc = getOrderSpecifier(klass, orderBy, pathBuilder);
            return (List<T>) query().from(pathBuilder).orderBy(asc).fetch();

        }
        return (List<T>) query().from(pathBuilder).fetch();
    }

    @Override
    public <T> List<T> getAllOrderBy(Class<T> klass, String orderby) {
        PathBuilder pathBuilder = getPathBuilder(klass);
        if (orderby != null) {
            OrderSpecifier asc = getOrderSpecifier(klass, orderby, pathBuilder);
            return (List<T>) query().from(pathBuilder).orderBy(asc).fetch();

        }
        return (List<T>) query().from(pathBuilder).fetch();
    }

    private <T> OrderSpecifier getOrderSpecifier(Class<T> klass, String orderby, PathBuilder pathBuilder) {
        Field field = ReflectionJavaUtil.getField(klass, orderby);
        return pathBuilder.getComparable(orderby, field.getType()).asc();
    }

    private <T> String getOrderBy(Class<T> klass) {
        String orderBy = "";
        if (klass.isAnnotationPresent(CrudOrderBy.class)) {
            orderBy = klass.getAnnotation(CrudOrderBy.class).value();
        }
        return orderBy;
    }

    @Override
    public <T> List<T> getAll(Class<T> klass, int firstResult, int maxResults) {
        PathBuilder<?> path = getPathBuilder(klass);
        CriteriaQuery<T> criteriaBuilder = getCriteriaBuilder(klass);
        TypedQuery<T> query = entityManager.createQuery(criteriaBuilder);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public <T> PathBuilder<?> getPathBuilder(Class<T> klass) {
        return new PathBuilder(klass, klass.getSimpleName().toLowerCase());
    }

    /**
     * Gets the current session.
     *
     * @return the current session
     */
    private final Session getCurrentSession() {
        return (Session) entityManager.getDelegate();
    }

    @Override
    public <T> T getById(Class<T> klass, Object key) {
        return entityManager.find(klass, key);
    }

    @Override
    public <T> List<T> getByCreateQuery(CriteriaQuery<T> criteria, Class<T> klass) {
        Root<T> from = criteria.from(klass);
        CriteriaQuery<T> select = criteria.select(from);
        return entityManager.createQuery(select).getResultList();
    }

    @Override
    public <T> CriteriaQuery<T> getCriteriaBuilder(Class<T> klass) {
        return getCriteriaBuilder().createQuery(klass);
    }

    private CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public <T> String getIdPropertyName(Class<T> klass) {
        return getCurrentSession().getSessionFactory().getClassMetadata(klass).getIdentifierPropertyName();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#getEntityByID(java.lang.
     * Class, long)
     */
    @Override
    public <T> T getEntityByID(Class<T> klass, long id) {
        return (T) getCurrentSession().get(klass, id);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#getEntityByQuery(java.lang
     * .String, java.lang.Object[])
     */
    @Override
    public List<T> getEntityByQuery(String query, Object... params) {
        Query q = getCurrentSession().createQuery(query);
        int i = 0;
        for (Object o : params) {
            q.setParameter(i, o);
            i++;
        }
        return (List<T>) q.list();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#getEntityByQueryUnique(
     * java.lang.String, java.lang.Object[])
     */
    @Override
    public <T> T getEntityByQueryUnique(String query, Object... params) {
        org.hibernate.query.Query q = getCurrentSession().createQuery(query);
        int i = 0;
        for (Object o : params) {
            q.setParameter(i, o);
            i++;
        }
        List<T> results = q.list();
        T foundentity = null;
        if (!results.isEmpty()) {
            foundentity = results.get(0);
        }
        return foundentity;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#save(java.lang.Object)
     */
    @Override
    public <T> T save(T klass) {
        getCurrentSession().saveOrUpdate(klass);
        //T t = updateFieldsEntites(klass);
        publishEvent(klass, "save");
        return klass;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.DAOGeneric#saveOrUpdate(java.lang.
     * Object)
     */
    @Override
    public <T> T saveOrUpdate(T klass) {
        publishEvent(klass, "save");
        T merge = entityManager.merge(klass);
        updateFieldsEntites(merge);
        return merge;
    }

    private <T> void publishEvent(T klass, String event) {
        try {
            EventQueues.lookup(event + klass.getClass().getSimpleName(), EventQueues.APPLICATION, true).publish(new Event(event + klass.getClass().getSimpleName(), null, null));
        } catch (Exception e) {
            LOGGER.error("ErrorSaveEventQueue", e);
        }
    }

    private <T> T updateFieldsEntites(T klass) {
        try {
            T merge = entityManager.merge(klass);
            Object idValue = ReflectionJavaUtil.getIdValue(klass);
            Object pass = getById(klass.getClass(), idValue);
            List<Field> list = ReflectionJavaUtil.getFields(klass.getClass());
            list.stream().filter(e -> e.isAnnotationPresent(ManyToOne.class)).forEach(e -> {
                Object valueFieldPass = ReflectionJavaUtil.getValueFieldObject(e.getName(), pass);
                Object valueFieldPresent = ReflectionJavaUtil.getValueFieldObject(e.getName(), klass);
                if (valueFieldPass != valueFieldPresent || (valueFieldPass == null && valueFieldPresent == null)) {
                    List<Field> listPass = ReflectionJavaUtil.getFields(valueFieldPass.getClass());
                    listPass.stream().filter(x -> x.getType() == List.class).filter(y -> ((ParameterizedType) y.getGenericType()).getActualTypeArguments()[0] == klass.getClass())
                            .forEach(z -> {
                                List valueFieldObject = (List) ReflectionJavaUtil.getValueFieldObject(z.getName(), valueFieldPass);
                                valueFieldObject.remove(pass);
                                ReflectionJavaUtil.setValueFieldObject(z.getName(), valueFieldPass, valueFieldObject);
                                entityManager.merge(valueFieldPass);
                            });
                    List<Field> listPresent = ReflectionJavaUtil.getFields(valueFieldPresent.getClass());
                    listPresent.stream().filter(x -> x.getType() == List.class).filter(y -> ((ParameterizedType) y.getGenericType()).getActualTypeArguments()[0] == klass.getClass())
                            .forEach(z -> {
                                List valueFieldObject = (List) ReflectionJavaUtil.getValueFieldObject(z.getName(), valueFieldPresent);
                                valueFieldObject.add(klass);
                                ReflectionJavaUtil.setValueFieldObject(z.getName(), valueFieldPresent, valueFieldObject);
                                entityManager.merge(valueFieldPresent);
                            });
                }
            });
            return merge;
        } catch (Exception ex) {
            LOGGER.error("Error", ex);
        }
        return klass;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.dao.GenericJPA#getEntityByQuery(java.lang
     * .String)
     */
    @Override
    public List<T> getEntityByQuery(String sqlquery) {
        SQLQuery q = getCurrentSession().createSQLQuery(sqlquery);
        return (List<T>) q.list();
    }

    @Override
    public <T> List<T> getLikePrecise(Class<T> klass, String text) {
        if (!text.isEmpty()) {
            return like(klass, "?" + text + "?");
        } else
            return getAllOrder(klass);
    }

    @Override
    public <T> List<T> getLikePrecise(Class<T> klass, String text, Predicate aditional) {
        if (!text.isEmpty()) {
            return getLike(klass, "?" + text + "?", aditional);
        } else
            return getAllOrder(klass);
    }

    @Override
    public <T> List<T> getBeginString(Class<T> klass, String text, String field, Predicate aditional) {
        if (!text.isEmpty()) {
            PathBuilder pathBuilder = getPathBuilder(klass);
            StringPath stringPath = pathBuilder.getString(field);
            BooleanExpression and = stringPath.startsWith(text).and(aditional);
            String orderBy = getOrderBy(klass);
            return (orderBy != null && !orderBy.isEmpty()) ?
                    (List<T>) query().from(pathBuilder).where(and).orderBy(getOrderSpecifier(klass, orderBy, pathBuilder)).fetch()
                    : (List<T>) query().from(pathBuilder).where(and).fetch();
        } else
            return getAllOrder(klass);
    }

    @Override
    public <T> List<T> getLike(Class<T> klass, String text) {
        return !text.isEmpty() ? like(klass, text) : getAllOrder(klass);
    }

    @Override
    public <T> List<T> getLike(Class<T> klass, String text, Predicate aditional) {
        List<Field> fields = ReflectionJavaUtil.getStringFields(klass);
        PathBuilder<?> pathBuilder = getPathBuilder(klass);
        Predicate like = null;
        if (aditional == null) {
            like = getLikePredicate(text, fields, pathBuilder, null);
        } else {
            like = getLikePredicate(text, fields, pathBuilder, aditional);
        }
        String orderBy = (String) getOrderBy(klass);
        return orderBy != null && !orderBy.isEmpty() ? (List<T>) query().from(pathBuilder).where(like).orderBy(getOrderSpecifier(klass, orderBy, pathBuilder)).fetch() : (List<T>) query().from(pathBuilder).where(like).fetch();
    }

    private <T> List<T> like(Class<T> klass, String text) {
        List<Field> fields = ReflectionJavaUtil.getStringFields(klass);
        PathBuilder<?> pathBuilder = getPathBuilder(klass);
        Predicate like = getLikePredicate(text, fields, pathBuilder, null);
        String orderBy = (String) getOrderBy(klass);
        if (orderBy != null && !orderBy.isEmpty()) {
            OrderSpecifier orderSpecifier = getOrderSpecifier(klass, orderBy, pathBuilder);
            return (List<T>) query().from(pathBuilder).where(like).orderBy(orderSpecifier).fetch();
        } else
            return (List<T>) query().from(pathBuilder).where(like).fetch();
    }

    @Override
    public Predicate getLikePredicate(String text, List<Field> fields, PathBuilder<?> pathBuilder, Predicate aditional) {
        Predicate like = null;
        if (!text.endsWith("?") && !text.startsWith("?"))
            for (Field f : fields) {
                String name = f.getName();
                if (like == null)
                    like = pathBuilder.getString(name).contains(text);
                else
                    like = pathBuilder.getString(name).contains(text).or(like);
            }
        else {
            String aux = text.substring(1, text.length() - 1);
            for (Field f : fields) {
                String name = f.getName();
                if (like == null)
                    like = pathBuilder.getString(name).eq(aux);
                else
                    like = pathBuilder.getString(name).eq(aux).or(like);
            }
        }
        return ((BooleanExpression) like).and(aditional);
    }

}

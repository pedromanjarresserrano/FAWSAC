package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.DAOGeneric;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * The Class GenericJPAServiceImpl.
 */
@Service
public class CrudServiceImpl implements CrudService {

    /**
     * The cruddao.
     */
    @Autowired
    private DAOGeneric cruddao;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.services.GenericJPAService#delete(java.
     * lang.Object)
     */
    @Transactional
    @Override
    public <T> void delete(T klass) {
        cruddao.delete(klass);
    }

    @Transactional
    @Override
    public <T> T refresh(T klass) {
        return cruddao.refresh(klass);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.services.GenericJPAService#getAll(java.
     * lang.Class)
     */
    @Transactional(readOnly = true)
    @Override
    public <T> List<T> getAll(Class<T> klass) {
        return cruddao.getAll(klass);
    }

    @Transactional(readOnly = true)
    @Override
    public <T> List<T> getAll(Class<T> klass, int firstResult, int maxResults) {
        return cruddao.getAll(klass, firstResult, maxResults);
    }

    @Transactional(readOnly = true)
    @Override
    public <T> List<T> getAllOrderBy(Class<T> klass, String orderby) {
        return cruddao.getAllOrderBy(klass, orderby);
    }


    @Transactional(readOnly = true)
    @Override
    public <T> List<T> getAllOrder(Class<T> klass) {
        return cruddao.getAllOrder(klass);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.services.GenericJPAService#getEntityByID(
     * java.lang.Class, long)
     */
    @Transactional
    @Override
    public <T> T getEntityByID(Class<T> klass, long id) {
        return cruddao.getEntityByID(klass, id);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.gitlab.pedrioko.services.GenericJPAService#
     * getEntityByQuery(java.lang.String, java.lang.Object[])
     */
    @Transactional
    @Override
    public <T> T getEntityByQuery(String query, Object... params) {
        return cruddao.getEntityByQuery(query, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.gitlab.pedrioko.services.GenericJPAService#
     * getEntityByQueryUnique(java.lang.String, java.lang.Object[])
     */
    @Transactional
    @Override
    public <T> T getEntityByQueryUnique(String query, Object... params) {
        return cruddao.getEntityByQueryUnique(query, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.services.GenericJPAService#save(java.lang
     * .Object)
     */
    @Transactional
    @Override
    public <T> void save(T klass) {
        cruddao.save(klass);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.services.CrudService#saveOrUpdate(java.
     * lang.Object)
     */
    @Override
    @Transactional(readOnly = false)
    public <T> T saveOrUpdate(T klass) {
        return cruddao.saveOrUpdate(klass);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.gitlab.pedrioko.services.GenericJPAService#
     * getEntityByQuery(java.lang.String)
     */
    @Transactional(readOnly = true)
    @Override
    public <T> T getEntityByQuery(String query) {
        return cruddao.getEntityByQuery(query);
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T getById(Class<T> klass, Object key) {
        return cruddao.getById(klass, key);
    }

    @Transactional
    @Override
    public <T> CriteriaQuery<T> getCriteriaBuilder(Class<T> klass) {
        return cruddao.getCriteriaBuilder(klass);
    }

    @Transactional
    @Override
    public <T> List<T> getByCreateQuery(CriteriaQuery<T> criteria, Class<T> klass) {
        return cruddao.getByCreateQuery(criteria, klass);
    }

    @Transactional

    @Override
    public <T> String getIdPropertyName(Class<T> klass) {
        return cruddao.getIdPropertyName(klass);
    }

    @Transactional
    @Override
    public <T> List<T> getLike(Class<T> klass, String text) {
        return cruddao.getLike(klass, text);
    }

    @Transactional
    @Override
    public <T> List<T> getLikePrecise(Class<T> klass, String text) {
        return cruddao.getLikePrecise(klass, text);
    }

    @Override
    public <T> List<T> getLikePrecise(Class<T> klass, String text, Predicate where) {
        return cruddao.getLikePrecise(klass, text, where);
    }

    @Transactional
    @Override
    public <T> List<T> getLike(Class<?> klass, String value, Predicate where) {
        return (List<T>) cruddao.getLike(klass, value, where);
    }

    @Transactional
    @Override
    public <T> List<T> getBeginString(Class<T> klass, String text, String field, Predicate aditional) {
        return cruddao.getBeginString(klass, text, field, aditional);
    }

    @Transactional
    @Override
    public <T> PathBuilder<?> getPathBuilder(Class<T> klass) {
        return cruddao.getPathBuilder(klass);
    }

    @Transactional
    @Override
    public JPAQuery<?> query() {
        return cruddao.query();
    }

    @Transactional
    @Override
    public JPAQuery<?> queryRand() {
        return cruddao.query();
    }
}

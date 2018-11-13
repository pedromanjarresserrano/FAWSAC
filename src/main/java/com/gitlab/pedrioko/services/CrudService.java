package com.gitlab.pedrioko.services;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * The Interface GenericJPAService.
 */
public interface CrudService {

    /**
     * Delete.
     *
     * @param <T>   the generic type
     * @param klass the klass
     */
    <T> void delete(T klass);

    <T> T refresh(T klass);

    /**
     * Gets the all.
     *
     * @param <T>   the generic type
     * @param klass the klass
     * @return the all
     */
    <T> List<T> getAll(Class<T> klass);

    <T> PathBuilder<?> getPathBuilder(Class<T> klass);

    JPAQuery<?> query();

    /**
     * Gets the entity by ID.
     *
     * @param <T>   the generic type
     * @param klass the klass
     * @param id    the id
     * @return the entity by ID
     */
    <T> T getEntityByID(Class<T> klass, long id);

    /**
     * Gets the entity by query.
     *
     * @param <T>    the generic type
     * @param query  the query
     * @param params the params
     * @return the entity by query
     */
    <T> T getEntityByQuery(String query, Object... params);

    /**
     * Gets the entity by query.
     *
     * @param <T>   the generic type
     * @param query the query
     * @return the entity by query
     */
    <T> T getEntityByQuery(String query);

    /**
     * Gets the entity by query unique.
     *
     * @param <T>    the generic type
     * @param query  the query
     * @param params the params
     * @return the entity by query unique
     */
    <T> T getEntityByQueryUnique(String query, Object... params);

    /**
     * Save.
     *
     * @param <T>   the generic type
     * @param klass the klass
     */
    <T> void save(T klass);

    /**
     * Save or update.
     *
     * @param <T>   the generic type
     * @param klass the klass
     * @return the t
     */
    <T> T saveOrUpdate(T klass);

    <T> T getById(Class<T> klass, Object key);

    <T> CriteriaQuery<T> getCriteriaBuilder(Class<T> klass);

    <T> List<T> getByCreateQuery(CriteriaQuery<T> criteria, Class<T> klass);

    @Transactional
    <T> String getIdPropertyName(Class<T> klass);

    <T> List<T> getLike(Class<T> klass, String text);

    <T> List<T> getLikePrecise(Class<T> klass, String text);

}

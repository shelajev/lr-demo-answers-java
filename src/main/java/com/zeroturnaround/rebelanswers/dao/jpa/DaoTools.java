package com.zeroturnaround.rebelanswers.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class DaoTools {

  @PersistenceContext
  private EntityManager entityManager;

  public DaoTools() {
  }

  public DaoTools(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public <T> T findById(final Class<T> entityClass, final Object id) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == id) throw new IllegalArgumentException("id can't be null");

    return entityManager.find(entityClass, id);
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> castResultList(List<?> results) {
    return (List<T>) results;
  }

  public <T> List<T> findByAttribute(final Class<T> entityClass, final String attributeName, final Object attributeValue) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == attributeName) throw new IllegalArgumentException("attributeName can't be null");
    if (null == attributeValue) throw new IllegalArgumentException("attributeValue can't be null");

    return castResultList(entityManager.createQuery(
        "select e from " + entityClass.getSimpleName() + " e where e." + attributeName + " = ?1").setParameter(1,
        attributeValue).getResultList());
  }

  public <T> List<T> getAllEntities(final Class<T> entityClass, final String orderByAttributeName,
                                    final SortOrder sortOrder) {
    return getFilteredEntities(entityClass, orderByAttributeName, sortOrder, "");
  }

  public <T> List<T> getFilteredEntities(final Class<T> entityClass, final String orderByAttributeName,
                                         final SortOrder sortOrder, final String filter) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == orderByAttributeName) throw new IllegalArgumentException("orderByAttributeName can't be null");
    if (null == filter) throw new IllegalArgumentException("filter can't be null");

    return castResultList(entityManager.createQuery(
        "select e from " + entityClass.getSimpleName() + " e " + filter + " order by e." + orderByAttributeName + " "
            + sortOrder.name()).getResultList());
  }

  public <T> T persist(final Class<T> entityClass, final T entity) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == entity) throw new IllegalArgumentException("entity can't be null");

    entityManager.persist(entity);
    return entity;
  }

  public <T> T merge(final Class<T> entityClass, final T entity) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == entity) throw new IllegalArgumentException("entity can't be null");

    return entityManager.merge(entity);
  }

  public long countEntities(final Class<?> entityClass) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");

    return (Long) entityManager.createQuery("select count(entity) from " + entityClass.getSimpleName() + " entity")
        .getSingleResult();
  }

  public long countEntitiesByAttribute(final Class<?> entityClass, final String attributeName,
                                       final Object attributeValue) {
    if (null == entityClass) throw new IllegalArgumentException("entityClass can't be null");
    if (null == attributeName) throw new IllegalArgumentException("attributeName can't be null");
    if (null == attributeValue) throw new IllegalArgumentException("attributeValue can't be null");

    return (Long) entityManager.createQuery(
        "select count(e) from " + entityClass.getSimpleName() + " e where e." + attributeName + " = ?1").setParameter(
        1, attributeValue).getSingleResult();
  }

  public enum SortOrder {
    ASC, DESC
  }
}

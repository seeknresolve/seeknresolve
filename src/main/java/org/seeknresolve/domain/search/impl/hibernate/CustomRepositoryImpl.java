package org.seeknresolve.domain.search.impl.hibernate;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.seeknresolve.domain.search.CustomRepository;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;
import org.springframework.data.history.Revisions;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoRepositoryBean
public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {
    private final EntityManager entityManager;

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> queryOnFields(String query, String... fields) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(getDomainClass()).get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword()
                .onFields(fields)
                .matching(query)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, getDomainClass());

        return jpaQuery.getResultList();
    }

    @Override
    @Transactional
    public Revisions<Integer, T> getAllRevisions(ID id) {
        AuditReader auditReaderFactory = AuditReaderFactory.get(entityManager);
        if(findOne(id) == null) {
            throw new EntityNotFoundException();
        }
        List<Number> revisionsNumbers = auditReaderFactory.getRevisions(getDomainClass(), id);
        Map<Number, T> revisionsMap = revisionsNumbers.stream()
                .collect(Collectors.toMap(Number::intValue, n -> auditReaderFactory.find(getDomainClass(), id, n)));
        return transformToRevisions(revisionsMap);
    }

    private Revisions<Integer, T> transformToRevisions(Map<Number, T> revisionsMap) {
        List<Revision<Integer, T>> revisionsList = new ArrayList<>(revisionsMap.size());

        for (Map.Entry<Number, T> revisionEntry : revisionsMap.entrySet()) {
            Integer revisionNumber = (Integer) revisionEntry.getKey();
            RevisionMetadata<Integer> revisionMetadata = getMetadata(revisionNumber, revisionEntry.getValue());
            Revision<Integer, T> revision = new Revision<>(revisionMetadata, revisionEntry.getValue());
            revisionsList.add(revision);
        }

        return new Revisions<>(revisionsList);
    }

    private RevisionMetadata<Integer> getMetadata(Integer revisionNumber, T entity) {
        return new RevisionMetadata<Integer>() {
            @Override
            public Integer getRevisionNumber() {
                return revisionNumber;
            }

            @Override
            public DateTime getRevisionDate() {
                Field dateField = findAnnotatedField(entity.getClass(), LastModifiedDate.class);
                if(dateField != null) {
                    dateField.setAccessible(true);
                    try {
                        return (DateTime) dateField.get(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            public T getDelegate() {
                return entity;
            }

            private Field findAnnotatedField(Class<? extends Object> clazz, Class<? extends Annotation> annotationClass) {
                for(Field field : clazz.getDeclaredFields()) {
                    if(field.getAnnotation(annotationClass) != null) {
                        return field;
                    }
                }
                return null;
            }
        };
    }

}

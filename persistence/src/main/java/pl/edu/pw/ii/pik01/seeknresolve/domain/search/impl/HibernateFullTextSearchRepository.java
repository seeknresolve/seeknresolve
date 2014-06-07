package pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.FullTextSearchRepository;

import javax.persistence.EntityManager;
import java.util.List;

public abstract class HibernateFullTextSearchRepository<DOMAIN_CLASS> implements FullTextSearchRepository<DOMAIN_CLASS> {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<DOMAIN_CLASS> queryOnFields(String query, String... fields) {
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

    public abstract Class<? extends DOMAIN_CLASS> getDomainClass();
}

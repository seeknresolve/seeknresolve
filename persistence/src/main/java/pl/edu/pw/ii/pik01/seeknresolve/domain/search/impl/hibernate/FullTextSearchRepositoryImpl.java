package pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl.hibernate;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.FullTextSearchRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public class FullTextSearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements FullTextSearchRepository<T, ID> {
    private EntityManager entityManager;

    public FullTextSearchRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
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
}

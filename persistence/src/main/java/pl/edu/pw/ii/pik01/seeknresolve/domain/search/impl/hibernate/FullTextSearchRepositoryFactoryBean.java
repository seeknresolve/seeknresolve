package pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.FullTextSearchRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
public class FullTextSearchRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, ID> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new HibernateFullTextSearchRepositoryFactory(entityManager);
    }

    private static class HibernateFullTextSearchRepositoryFactory extends JpaRepositoryFactory {
        private EntityManager entityManager;

        private HibernateFullTextSearchRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @Override
        protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {
            return new FullTextSearchRepositoryImpl<>((Class<T>)metadata.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return FullTextSearchRepository.class;
        }
    }
}

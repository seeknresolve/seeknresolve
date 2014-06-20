package org.seeknresolve.infrastructure.indexing;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@Profile("development")
public class DatabaseIndexer {
    private static final Logger log = LoggerFactory.getLogger(DatabaseIndexer.class);

    @Autowired
    public DatabaseIndexer(EntityManager entityManager) throws InterruptedException {
        startIndexing(entityManager);
    }

    private void startIndexing(EntityManager entityManager) throws InterruptedException {
        log.info("Database indexing started.");
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
        log.info("Database indexing finished.");
    }
}

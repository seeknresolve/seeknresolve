package org.seeknresolve;

import org.seeknresolve.domain.search.impl.hibernate.SearchWithAuditRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ComponentScan
@EnableAutoConfiguration(exclude = { LiquibaseAutoConfiguration.class })
@EnableJpaRepositories(value = "org.seeknresolve.domain",
        repositoryFactoryBeanClass = SearchWithAuditRepositoryFactoryBean.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
@Profile("test")
public class TestAppContext {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestAppContext.class);
        app.setAdditionalProfiles("test");
        app.setShowBanner(false);
        app.run(args);
    }
}

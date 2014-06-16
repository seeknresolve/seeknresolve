package pl.edu.pw.ii.pik01.seeknresolve;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl.hibernate.CustomRepositoryFactoryBean;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories(value = "pl.edu.pw.ii.pik01.seeknresolve.domain",
    repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
@Profile("development")
public class SeekNResolve implements EmbeddedServletContainerCustomizer {
    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SeekNResolve.class);
        app.setAdditionalProfiles("development");
        app.run(args);
    }

    @Bean
    public Module getJodaTimeModule() {
        return new JodaModule();
    }

    @Bean
    @Profile("development")
    public SpringLiquibase getSpringLiquibase() {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setContexts("development, test");
        springLiquibase.setChangeLog("classpath:liquibase/db-changelog.xml");
        springLiquibase.setDataSource(dataSource);
        return springLiquibase;
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
    }
}

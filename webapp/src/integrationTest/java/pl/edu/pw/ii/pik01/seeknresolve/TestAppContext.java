package pl.edu.pw.ii.pik01.seeknresolve;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.edu.pw.ii.pik01.seeknresolve.domain.search.impl.hibernate.CustomRepositoryFactoryBean;

@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories(value = "pl.edu.pw.ii.pik01.seeknresolve.domain",
        repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class TestAppContext {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestAppContext.class);
        app.setShowBanner(false);
        app.run(args);
    }

    @Bean
    public Module getJodaTimeModule() {
        return new JodaModule();
    }
}

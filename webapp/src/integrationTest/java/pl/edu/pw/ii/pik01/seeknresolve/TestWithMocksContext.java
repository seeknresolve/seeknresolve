package pl.edu.pw.ii.pik01.seeknresolve;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class TestWithMocksContext {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TestWithMocksContext.class);
        app.setShowBanner(false);
        app.run(args);
    }

    @Bean
    public Module getJodaTimeModule() {
        return new JodaModule();
    }
}

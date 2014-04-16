package pl.edu.pw.ii.pik01.seeknresolve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class SeekNResolve {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SeekNResolve.class);
        app.setShowBanner(false);
        app.run(args);
    }
}

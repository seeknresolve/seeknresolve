package org.seeknresolve;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebXml extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        applicationBuilder.application().setAdditionalProfiles("development");
        return applicationBuilder.sources(SeekNResolve.class);
    }
}

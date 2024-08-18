package com.search.vehicle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Includes the web configuration (CORS/etc.).
 * @author Vansh Pratap Singh
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${application.cors.mapping.path}")
    private String applicationCorsMappingPath;

    @Value("${application.cors.allowed.origins}")
    private String applicationCorsAllowedOrigins;

    @Value("${application.cors.allowed.headers}")
    private String applicationCorsAllowedHeaders;

    @Value("${application.cors.allow.credentials}")
    private Boolean applicationCorsAllowCredentials;

    /**
     * Handles the CORS Mapping, Origins, methods, headers, etc.
     * @param registry          CorsRegistry object.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
        registry.addMapping(applicationCorsMappingPath)
                .allowedOrigins(applicationCorsAllowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders(applicationCorsAllowedHeaders)
                .allowCredentials(applicationCorsAllowCredentials);

    }

}

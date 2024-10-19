package io.javabrains.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // You can specify more restrictive patterns here
                    .allowedOrigins("*") // Specify the allowed origins
                    .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed methods
                    .allowedHeaders("*") // Specify the allowed headers
                    .allowCredentials(true) // Whether to send cookies
                    .maxAge(3600); // Max age until the browser caches the preflight request response
            }
        };
    }
}

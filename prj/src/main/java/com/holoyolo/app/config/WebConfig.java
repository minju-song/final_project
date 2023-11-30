package com.holoyolo.app.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   
   @Value("${file.loading.path}")
   private String loadingPath;
   
   @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      System.out.println(loadingPath);
        registry.addResourceHandler("/images/**")
                .addResourceLocations(loadingPath)
                .setCachePeriod(100);
    }

}

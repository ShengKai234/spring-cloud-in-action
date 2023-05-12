package com.thoughtmechanix.licensingservice;

import com.thoughtmechanix.licensingservice.utils.UserContextInterceptor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.catalina.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
@EnableFeignClients
public class LicensingServiceApplication {

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.US);
    return localeResolver;
  }

  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setUseCodeAsDefaultMessage(true);     // Don't throw an error if a msg not found.
    messageSource.setBasenames("messages");             // Sets the base name of the languages properties files.
    return messageSource;
  }

  // use Ribbon-backed with RestTemplate
  @SuppressWarnings("unchecked")
  @LoadBalanced
  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplate template = new RestTemplate();
    List interceptors = template.getInterceptors();
    if (interceptors != null) {
      template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
    } else {
      interceptors.add(new UserContextInterceptor());
      template.setInterceptors(interceptors);
    }
    return template;
  }

  public static void main(String[] args) {
    SpringApplication.run(LicensingServiceApplication.class, args);
  }

}

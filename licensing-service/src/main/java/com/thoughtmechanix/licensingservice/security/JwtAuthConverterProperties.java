package com.thoughtmechanix.licensingservice.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtAuthConverterProperties {
  //  to get those config properties into the Spring Boot application inside the security package
  private String resourceId;
  private String principalAttribute;
}

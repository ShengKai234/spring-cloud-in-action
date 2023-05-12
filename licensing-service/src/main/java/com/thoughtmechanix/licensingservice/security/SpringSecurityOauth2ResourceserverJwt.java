package com.thoughtmechanix.licensingservice.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.jwt")
@Getter
@Setter
@RefreshScope
public class SpringSecurityOauth2ResourceserverJwt {
  private String jwkSetUri;

}

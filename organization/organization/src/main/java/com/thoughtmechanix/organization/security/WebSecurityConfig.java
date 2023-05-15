package com.thoughtmechanix.organization.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

//  private final KeycloakLogoutHandler keycloakLogoutHandler;
  public static final String ADMIN = "ADMIN";
  public static final String USER = "USER";

  @Autowired
  private final JwtAuthConverter jwtAuthConverter;

  @Autowired
  private final SpringSecurityOauth2ResourceserverJwt springSecurityOauth2ResourceserverJwt;

  @Bean
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }


  @Bean
  public JwtDecoder jwtDecoder() {
//    return NimbusJwtDecoder.withJwkSetUri("http://keycloak:8080/auth/realms/spmia-realm/protocol/openid-connect/certs").build();
    return NimbusJwtDecoder.withJwkSetUri(springSecurityOauth2ResourceserverJwt.getJwkSetUri()).build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/test/anonymous", "/test/anonymous/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/test/admin", "/test/admin/**").hasAnyRole(ADMIN)
        .requestMatchers(HttpMethod.GET, "/test/user").hasAnyRole(ADMIN, USER)
        .requestMatchers(HttpMethod.POST, "/v1/organizations", "v1/organizations/**").hasAnyRole(ADMIN, USER)
        .anyRequest().permitAll();
    http.oauth2ResourceServer()
        .jwt()
        .decoder(jwtDecoder())
        .jwtAuthenticationConverter(jwtAuthConverter);
//    http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    http.csrf().disable();
    return http.build();
  }


}

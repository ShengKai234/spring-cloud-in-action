package com.thoughtmechanix.gatewaysvr.filter;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TrackingFilter implements GlobalFilter {
  private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

  @Autowired
  FilterUtils filterUtils;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // Code that executes every time a request passes through the filter

    // Extracts the HTTP header from the request using the ServerWebExchange object passed by parameters to the filter() method
    HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
    if (isCorrelationIdPresent(requestHeaders)) {
      logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId(requestHeaders));
    } else {
      String correlationId = generateCorrelationId();
      exchange = filterUtils.setCorrelationId(exchange, correlationId);
      logger.debug("tmx-correlation-id generated in tracking filter: {}. ", correlationId);
    }

    System.out.println("The authentication name from the token is : " + getUsername(requestHeaders));

    return chain.filter(exchange);
  }



  private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
    // help method to check correlation id in the request header
    if (filterUtils.getCorrelationId(requestHeaders) != null) {
      return true;
    } else {
      return false;
    }
  }

  private String generateCorrelationId() {
    return java.util.UUID.randomUUID().toString();
  }

  private String getUsername(HttpHeaders requestHeaders) {
    String username = "";
    if (filterUtils.getAuthorization(requestHeaders) != null) {
      String authorization = filterUtils.getAuthorization(requestHeaders).replace("Bearer ", "");
      JSONObject jsonObj = decodeJWT(authorization);
      try {
        username = jsonObj.getString("preferred_username");
      } catch (Exception e) {
        logger.debug(e.getMessage());
      }
    }
    return username;
  }

  private JSONObject decodeJWT(String JWTToken) {
    String[] split_string = JWTToken.split("\\.");
    String base64EncodedBody = split_string[1];
    Base64 base64Url = new Base64(true);
    String body = new String(base64Url.decode(base64EncodedBody));
    JSONObject jsonObj = new JSONObject(body);
    return jsonObj;
  }
}

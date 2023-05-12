package com.thoughtmechanix.gatewaysvr.filter;


import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtils {

  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String AUTH_TOKEN = "tmx-auth-token";
  public static final String USER_ID = "tmx-user-id";
  public static final String AUTHORIZATION = "Authorization";

  public String getCorrelationId(HttpHeaders requestHeaders) {
    if (requestHeaders.get(CORRELATION_ID) != null) {
      List<String> header = requestHeaders.get(CORRELATION_ID);
      return header.stream().findFirst().get();
    } else {
      return null;
    }
  }

  public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
    return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
  }

  private ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
    // This method returns a builder to mutate properties of the exchange object
    // by wrapping it with ServerWebExchangeDecorator and either returning mutated values
    // or delegating it back to this instance.
    return exchange.mutate().request(
        exchange.getRequest().mutate()
            .header(name, value)
            .build())
        .build();
  }

  public String getAuthorization(HttpHeaders requestHeaders) {
    if (requestHeaders.get(AUTHORIZATION) != null) {
      List<String> header = requestHeaders.get(AUTHORIZATION);
      return header.stream().findFirst().get();
    } else {
      return null;
    }
  }
}

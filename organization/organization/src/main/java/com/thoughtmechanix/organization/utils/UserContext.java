package com.thoughtmechanix.organization.utils;

import org.springframework.http.HttpHeaders;

public class UserContext {
  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String AUTH_TOKEN     = "tmx-auth-token";
  public static final String USER_ID        = "tmx-user-id";
  public static final String ORG_ID         = "tmx-org-id";
  public static final String AUTHORIZATION         = "Authorization";

  private static ThreadLocal<String> correlationId= new ThreadLocal<String>();
  private static ThreadLocal<String> authToken= new ThreadLocal<String>();
  private static ThreadLocal<String> userId = new ThreadLocal<String>();
  private static ThreadLocal<String> orgId = new ThreadLocal<String>();
  private static ThreadLocal<String> authorization = new ThreadLocal<String>();

  public static String getCorrelationId() { return correlationId.get();}
  public static void setCorrelationId(String cid) {
    correlationId.set(cid);
  }

  public static String getAuthToken() {
    return authToken.get();
  }

  public static void setAuthToken(String aToken) {
    authToken.set(aToken);
  }

  public static String getUserId() {
    return userId.get();
  }

  public static void setUserId(String aUserId) {
    userId.set(aUserId);
  }

  public static String getOrgId() {
    return orgId.get();
  }

  public static void setOrgId(String aOrgId) {
    orgId.set(aOrgId);
  }

  public static String getAuthorization() {
    return authorization.get();
  }

  public static void setAuthorization(String aAuthorization) {
    authorization.set(aAuthorization);
  }

  public static HttpHeaders getHttpHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set(CORRELATION_ID, getCorrelationId());

    return httpHeaders;
  }
}

package com.thoughtmechanix.licensingservice.client;

import com.thoughtmechanix.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

// by using the RestTemplate class, Ribbon will round-robin load balance
// all requests among all the service instances.

@Component
public class OrganizationRestTemplateClient {
  @Autowired
  RestTemplate restTemplate;

  public Organization getOrganization(String organizationId) {

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + "");

    ResponseEntity<Organization> restExchange = restTemplate.exchange(
        "http://organizationservice/v1/organizations/{organizationId}",
        HttpMethod.GET,
        null,
        Organization.class,
        organizationId
    );
    return restExchange.getBody();
  }
}

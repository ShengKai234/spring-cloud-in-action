package com.thoughtmechanix.licensingservice.services.client;

import com.thoughtmechanix.licensingservice.model.Organization;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationDiscoveryClient {

  @Autowired
  private DiscoveryClient discoveryClient;

  public Organization getOrganization(String organizationId) {
    RestTemplate restTemplate = new RestTemplate();

    // get service list by passing in the key of service
    List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

    if (instances.size() == 0) return null;

    // combine the uri and parameter
    String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

    // call service
    ResponseEntity<Organization> restExchange = restTemplate.exchange(serviceUri, HttpMethod.GET, null, Organization.class, organizationId);

    return restExchange.getBody();
  }
}

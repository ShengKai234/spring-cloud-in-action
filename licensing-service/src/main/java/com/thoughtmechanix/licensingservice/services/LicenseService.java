package com.thoughtmechanix.licensingservice.services;

import com.thoughtmechanix.licensingservice.client.OrganizationDiscoveryClient;
import com.thoughtmechanix.licensingservice.client.OrganizationFeignClient;
import com.thoughtmechanix.licensingservice.client.OrganizationRestTemplateClient;
import com.thoughtmechanix.licensingservice.config.ServiceConfig;
import com.thoughtmechanix.licensingservice.model.License;
import com.thoughtmechanix.licensingservice.model.Organization;
import com.thoughtmechanix.licensingservice.repository.LicenseRepository;
import com.thoughtmechanix.licensingservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class LicenseService {
  private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

  @Autowired
  MessageSource messages;

  @Autowired
  private LicenseRepository licenseRepository;

  @Autowired
  ServiceConfig config;

  @Autowired
  OrganizationFeignClient organizationFeignClient;

  @Autowired
  OrganizationRestTemplateClient organizationRestTemplateClient;

  @Autowired
  OrganizationDiscoveryClient organizationDiscoveryClient;

  public Organization retrieveOrgInfo(String organizationId, String clientType) {
    Organization organization = null;

    switch (clientType) {
      case "feign":
        System.out.println("I am using the feign client");
        organization = organizationFeignClient.getOrganization(organizationId);
        break;
      case "rest":
        System.out.println("I am using the rest client");
        organization = organizationRestTemplateClient.getOrganization(organizationId);
        break;
      case "discovery":
        System.out.println("I am using the discovery client");
        organization = organizationDiscoveryClient.getOrganization(organizationId);
        break;
      default:

    }
    return organization;
  }

  private void randomlyRunLong() throws TimeoutException {
    Random rand = new Random();

    int randomNum = rand.nextInt(3) + 1;

    if (randomNum == 3) {
//      System.out.println("sleep...");
      sleep();
    }
  }

  private void sleep() throws TimeoutException {
    try {
      System.out.println("Sleep");
      Thread.sleep(5000);
      throw new java.util.concurrent.TimeoutException();
    } catch (InterruptedException e) {
      logger.error(e.getMessage());
    }
  }

  public License getLicense(String organizationId, String licenseId) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    return license.withComment(config.getProperty());
  }

  public License getLicense(String organizationId, String licenseId, String clientType) {
    License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

    if (null == license) {
      throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null), licenseId, organizationId));
    }

    // use retrieveOrgInfo() to retrieve the organization data from the Postgres database.
    Organization org = retrieveOrgInfo(organizationId, clientType);
    if (null != org) {
      license.setOrganizationName(org.getName());
      license.setContactName(org.getContactName());
      license.setContactEmail(org.getContactEmail());
      license.setContactPhone(org.getContactPhone());
    }

    return license.withComment(config.getProperty());
  }

  public License createLicense(License license) {
    license.setLicenseId(UUID.randomUUID().toString());
    licenseRepository.save(license);

    return license.withComment(config.getProperty());
  }

  public License updateLicense(License license) {
    licenseRepository.save(license);

    return license.withComment(config.getProperty());
  }

  public String deleteLicense(String licneseId) {
    String responseMsg = null;
    License license = new License();
    license.setLicenseId(licneseId);
    licenseRepository.delete(license);
    responseMsg = String.format(messages.getMessage("license.delete.message", null, null), licneseId);
    return responseMsg;
  }

  @CircuitBreaker(name= "licenseService", fallbackMethod = "buildFallbackLicenseList")
  @RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
  @Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
  @Bulkhead(name = "bulkheadLicenseService", type = Type.THREADPOOL, fallbackMethod = "buildFallbackLicenseList")
  public CompletableFuture<List<License>> getLicensesByOrg(String organizationId) throws TimeoutException {
    logger.debug("LicenseService.getLiscensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
    System.out.println("resilience4j command: " + UserContextHolder.getContext().getCorrelationId());
    randomlyRunLong();
    return CompletableFuture.completedFuture(licenseRepository.findByOrganizationId(organizationId));
  }

  @SuppressWarnings("unused")
  private CompletableFuture<List<License>> buildFallbackLicenseList(String organizationId, Throwable t){
    List<License> fallbackList = new ArrayList<>();
    License license = new License();
    license.setLicenseId("0000000-00-00000");
    license.setOrganizationId(organizationId);
    license.setProductName("Sorry no licensing information currently available");
    fallbackList.add(license);
    return CompletableFuture.completedFuture(fallbackList);
  }
}

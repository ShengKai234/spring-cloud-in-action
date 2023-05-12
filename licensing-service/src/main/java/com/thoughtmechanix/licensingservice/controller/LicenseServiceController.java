package com.thoughtmechanix.licensingservice.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.thoughtmechanix.licensingservice.config.ServiceConfig;
import com.thoughtmechanix.licensingservice.model.License;
import com.thoughtmechanix.licensingservice.services.LicenseService;
import com.thoughtmechanix.licensingservice.utils.UserContextHolder;
import jakarta.annotation.security.RolesAllowed;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

  private static final Logger logger = LoggerFactory.getLogger(LicenseServiceController.class);

  @Autowired
  private LicenseService licenseService;
  @Autowired
  ServiceConfig serviceConfig;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public CompletableFuture<List<License>> getLicenses(
      @PathVariable("organizationId") String organizationId) throws TimeoutException {
    logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
    return licenseService.getLicensesByOrg(organizationId);
  }

  @RolesAllowed({"ADMIN", "USER"})
  @RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
  public ResponseEntity<License> getLicenses(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId) {
    License license = licenseService.getLicense(organizationId, licenseId, "");
    license.add(
        linkTo(methodOn(LicenseServiceController.class).getLicenses(organizationId, license.getLicenseId())).withSelfRel(),
        linkTo(methodOn(LicenseServiceController.class).createLicense(license)).withRel("createLicense"),
        linkTo(methodOn(LicenseServiceController.class).updateLicense(license)).withRel("updateLicense"),
        linkTo(methodOn(LicenseServiceController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
      );
    return ResponseEntity.ok(license);
  }

  @RolesAllowed({"ADMIN", "USER"})
  @RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
  public License getLicensesWithClient(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId,
      @PathVariable("clientType") String clientType) {
    return licenseService.getLicense(organizationId, licenseId, clientType);
  }

  @PutMapping
  public ResponseEntity<License> updateLicense(@RequestBody License request) {
    return ResponseEntity.ok(licenseService.updateLicense(request));
  }

  @PostMapping
  public ResponseEntity<License> createLicense(@RequestBody License request) {
    return ResponseEntity.ok(licenseService.createLicense(request));
  }

  @DeleteMapping(value = "/{licenseId}")
  public ResponseEntity<String> deleteLicense(@PathVariable("licenseId") String licenseId){
    return ResponseEntity.ok(licenseService.deleteLicense(licenseId));
  }



}

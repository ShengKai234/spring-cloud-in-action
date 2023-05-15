package com.thoughtmechanix.organization.controllers;

import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.services.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {

  @Autowired
  private OrganizationService orgService;

  @RolesAllowed({"ADMIN", "USER"})
  @CrossOrigin(origins = "*")
  @PostMapping
  public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
    System.err.println("saveOrganization...");
    return ResponseEntity.ok(orgService.create(organization));
  }

  @RolesAllowed({"ADMIN", "USER"})
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ResponseEntity<Organization> saveOrganization() {
    System.err.println("saveOrganization...");
    Organization organization = new Organization();
    organization.setName("Ostock");
    organization.setContactName("illary Huaylupo");
    organization.setContactEmail("illaryhs@gmail.com");
    organization.setContactPhone("88888888");

    return ResponseEntity.ok(orgService.create(organization));
  }

  @RolesAllowed({"ADMIN", "USER"})
  @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
  public Object getOrganization(@PathVariable("organizationId") String organizationId) {
    System.out.println("organizationId: 123" + organizationId);
    return orgService.getOrg(organizationId);
  }
}

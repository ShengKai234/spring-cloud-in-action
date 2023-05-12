package com.thoughtmechanix.organization.controllers;

import com.thoughtmechanix.organization.services.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {

  @Autowired
  private OrganizationService orgService;

  @RolesAllowed({"ADMIN", "USER"})
  @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
  public Object getOrganization(@PathVariable("organizationId") String organizationId) {
    System.out.println("organizationId: 123" + organizationId);
    return orgService.getOrg(organizationId);
  }
}

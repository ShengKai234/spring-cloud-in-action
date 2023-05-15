package com.thoughtmechanix.organization.services;

import com.thoughtmechanix.organization.events.source.SimpleSourceBean;
import com.thoughtmechanix.organization.model.Organization;
import com.thoughtmechanix.organization.repository.OrganizationRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
  private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
  @Autowired
  private OrganizationRepository orgRepository;

  @Autowired
  SimpleSourceBean simpleSourceBean;

  public Organization create(Organization organization) {
    organization.setOrganizationId(UUID.randomUUID().toString());
    organization = orgRepository.save(organization);
    simpleSourceBean.publishOrganizationChange("SAVE", organization.getOrganizationId());
    return organization;
  }

  public Organization getOrg(String organizationId) {
    return orgRepository.findByOrganizationId(organizationId);
  }

  public void saveOrg(Organization org){
    org.setOrganizationId( UUID.randomUUID().toString());

    orgRepository.save(org);

  }

  public void updateOrg(Organization org){
    orgRepository.save(org);
  }

//  public void deleteOrg(Organization org){
//    orgRepository.delete(org.getOrganizationId());
//  }
}

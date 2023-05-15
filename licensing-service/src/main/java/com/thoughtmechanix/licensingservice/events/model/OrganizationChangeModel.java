package com.thoughtmechanix.licensingservice.events.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrganizationChangeModel {
  private String type;
  private String action;
  private String organizationId;
  private String correlationId;

//  public OrganizationChangeModel(){
//    super();
//  }
//
//  public  OrganizationChangeModel(String type, String action, String organizationId, String correlationId) {
//    super();
//    this.type   = type;
//    this.action = action;
//    this.organizationId = organizationId;
//    this.correlationId = correlationId;
//  }
}

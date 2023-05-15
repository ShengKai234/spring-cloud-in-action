package com.thoughtmechanix.licensingservice.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @ToString
@RedisHash("organization") // store in redis cache
public class Organization extends RepresentationModel<Organization> {

  @Id
  String id;

  String name;
  String contactName;
  String contactEmail;
  String contactPhone;
}

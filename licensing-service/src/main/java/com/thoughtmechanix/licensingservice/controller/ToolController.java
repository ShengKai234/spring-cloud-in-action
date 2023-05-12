package com.thoughtmechanix.licensingservice.controller;

import com.thoughtmechanix.licensingservice.services.DiscoveryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "v1/tools")
public class ToolController {

  @Autowired
  DiscoveryService discoveryService;

  @RequestMapping(value = "/eureka/services", method = RequestMethod.GET)
  public List<String> getEurekaServices() {
    return discoveryService.getEurekaServices();
  }
}

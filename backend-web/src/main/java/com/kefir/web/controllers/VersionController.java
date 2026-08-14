package com.kefir.web.controllers;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/version")
public class VersionController {

  @GetMapping
  public Map<String, String> version() {
    return Map.of("version", "1.0.3");
  }
}
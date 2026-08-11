package com.kefir.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/version")
public class VersionController {

  @GetMapping
  public Map<String, String> version() {
    return Map.of("version", "v2");
  }
}
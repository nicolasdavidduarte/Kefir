package com.kefir.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuxAuthService {

  private final CoreUserService coreUserService;

  public AuxAuthService(CoreUserService coreUserService) {
    this.coreUserService = coreUserService;
  }

  public Integer retrieveUserIdFromAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    return coreUserService.fetchIdByUsername(auth.getPrincipal().toString());
  }
}

package com.kefir.services;

import com.kefir.entities.CoreUser;
import com.kefir.exceptions.CoreUserNotFoundException;
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

  public CoreUser retrieveUserFromAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    return coreUserService
        .fetchByUsername(auth.getPrincipal().toString())
        .orElseThrow(() -> new CoreUserNotFoundException("User not found"));
  }
}

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

  public CoreUser getUserFromAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    return coreUserService
        .getByUsername(auth.getPrincipal().toString());
  }
}

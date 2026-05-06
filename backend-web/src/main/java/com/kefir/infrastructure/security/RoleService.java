package com.kefir.infrastructure.security;

import com.kefir.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }
}

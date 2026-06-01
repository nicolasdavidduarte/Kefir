package com.kefir.services;

import com.kefir.entities.Role;
import com.kefir.enums.UserRoles;
import com.kefir.exceptions.RolesNotValidException;
import com.kefir.repositories.RoleRepository;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public Set<Role> getRolesByName(List<UserRoles> userRoles) {
    List<String> roleNames = userRoles.stream().map(UserRoles::name).toList();

    Set<Role> roles = roleRepository.findByNamesIgnoreCase(roleNames);

    if (roles.size() != roleNames.size()) {
      throw new RolesNotValidException();
    }

    return roles;
  }
}

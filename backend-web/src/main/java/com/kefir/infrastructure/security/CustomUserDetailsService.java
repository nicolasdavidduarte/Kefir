package com.kefir.infrastructure.security;

import com.kefir.entities.CoreUser;
import com.kefir.repositories.CoreUserRepository;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final CoreUserRepository coreUserRepository;

  public CustomUserDetailsService(CoreUserRepository coreUserRepository) {
    this.coreUserRepository = coreUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    CoreUser coreUser =
        coreUserRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<GrantedAuthority> authorities =
        coreUser.getRoles().stream()
            .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .toList();

    return new org.springframework.security.core.userdetails.User(
        coreUser.getUsername(), coreUser.getPassword(), authorities);
  }
}

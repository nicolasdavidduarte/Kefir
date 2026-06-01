package com.kefir.infrastructure.security;

import com.kefir.entities.User;
import com.kefir.repositories.UserRepository;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    final User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    final List<GrantedAuthority> authorities =
        user.getRoles().stream()
            .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .toList();

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), user.getPassword(), authorities);
  }
}

package com.kefir.services;

import com.kefir.entities.CoreUser;
import com.kefir.exceptions.CoreUserNotFoundException;
import com.kefir.repositories.CoreUserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@EnableCaching
@Service
public class CoreUserService {

  private final CoreUserRepository coreUserRepository;

  public CoreUserService(CoreUserRepository coreUserRepository) {
    this.coreUserRepository = coreUserRepository;
  }

  public List<CoreUser> findAll() {
    return coreUserRepository.findAll();
  }

  public Optional<CoreUser> findById(Long id) {
    return coreUserRepository.findById(id);
  }

  @Cacheable(value = "usersByUsername", key = "#username")
  public Integer fetchIdByUsername(String username) {
    return coreUserRepository
        .findIdByUsername(username)
        .orElseThrow(CoreUserNotFoundException::new);
  }
}

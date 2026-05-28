package com.kefir.services;

import com.kefir.entities.CoreUser;
import com.kefir.exceptions.CoreUserNotFoundException;
import com.kefir.repositories.CoreUserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@EnableCaching
@Service
public class CoreUserService {

  private final CoreUserRepository coreUserRepository;

  public CoreUserService(CoreUserRepository coreUserRepository) {
    this.coreUserRepository = coreUserRepository;
  }

  public List<CoreUser> getAll() {
    return coreUserRepository.findAll();
  }

  public Optional<CoreUser> getById(Integer id) {
    return coreUserRepository.findById(id);
  }

  public CoreUser getByUsername(String username) {
    return coreUserRepository
        .findByUsername(username)
        .orElseThrow(() -> new CoreUserNotFoundException("User not found"));
  }
}

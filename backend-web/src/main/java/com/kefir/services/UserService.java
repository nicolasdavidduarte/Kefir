package com.kefir.services;

import com.kefir.entities.CoreUser;
import com.kefir.exceptions.CoreUserNotFoundException;
import com.kefir.repositories.CoreUserRepository;
import java.util.List;
import java.util.Optional;

import com.kefir.web.dtos.UserResponse;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@EnableCaching
@Service
public class CoreUserService {

  private final CoreUserRepository coreUserRepository;

  public CoreUserService(CoreUserRepository coreUserRepository) {
    this.coreUserRepository = coreUserRepository;
  }

  public List<UserResponse> getAll() {
    return coreUserRepository.findAll().stream().map(UserResponse::fromEntity).toList();
  }

  public UserResponse getById(Integer id) {
    return coreUserRepository.findById(id).map(UserResponse::fromEntity)
            .orElseThrow(CoreUserNotFoundException::new);
  }

  public CoreUser getByUsername(String username) {
    return coreUserRepository.findByUsername(username).orElseThrow(CoreUserNotFoundException::new);
  }
}

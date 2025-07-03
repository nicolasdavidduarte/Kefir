package org.kefir.services;

import java.util.List;
import java.util.Optional;
import org.kefir.entities.CoreUser;
import org.kefir.repositories.CoreUserRepository;
import org.springframework.stereotype.Service;

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
}

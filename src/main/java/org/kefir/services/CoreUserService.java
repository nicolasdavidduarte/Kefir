package org.kefir.services;

import org.kefir.entities.CoreUser;
import org.kefir.repositories.CoreUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
package org.kefir.service;

import org.kefir.entity.SystemUser;
import org.kefir.repository.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;

    public SystemUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    public List<SystemUser> findAll() {
        return systemUserRepository.findAll();
    }

    public Optional<SystemUser> findById(Long id) {
        return systemUserRepository.findById(id);
    }


}
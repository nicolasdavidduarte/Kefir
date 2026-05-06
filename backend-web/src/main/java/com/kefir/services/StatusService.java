package com.kefir.services;

import com.kefir.entities.Status;
import com.kefir.repositories.StatusRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

  private final StatusRepository statusRepository;

  public StatusService(StatusRepository statusRepository) {
    this.statusRepository = statusRepository;
  }

  public List<Status> findAll() {
    return statusRepository.findAll();
  }

  public Optional<Status> findById(Long id) {
    return statusRepository.findById(id);
  }
}

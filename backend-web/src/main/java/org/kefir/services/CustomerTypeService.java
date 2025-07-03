package org.kefir.services;

import java.util.List;
import java.util.Optional;
import org.kefir.entities.CustomerType;
import org.kefir.repositories.CustomerTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerTypeService {

  private final CustomerTypeRepository customerTypeRepository;

  public CustomerTypeService(CustomerTypeRepository customerTypeRepository) {
    this.customerTypeRepository = customerTypeRepository;
  }

  public List<CustomerType> findAll() {
    return customerTypeRepository.findAll();
  }

  public Optional<CustomerType> findById(Long id) {
    return customerTypeRepository.findById(id);
  }
}

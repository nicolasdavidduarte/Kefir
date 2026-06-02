package com.kefir.services;

import com.kefir.entities.CustomerType;
import com.kefir.exceptions.CustomerTypeNotFoundException;
import com.kefir.repositories.CustomerTypeRepository;
import com.kefir.web.dtos.customerType.CustomerTypeResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerTypeService {

  private final CustomerTypeRepository customerTypeRepository;

  public CustomerTypeService(CustomerTypeRepository customerTypeRepository) {
    this.customerTypeRepository = customerTypeRepository;
  }

  public List<CustomerTypeResponse> getAll() {
    return customerTypeRepository.findAll().stream().map(CustomerTypeResponse::fromEntity).toList();
  }

  public CustomerTypeResponse getById(Integer id) {
    CustomerType customerType =
        customerTypeRepository.findById(id).orElseThrow(CustomerTypeNotFoundException::new);

    return CustomerTypeResponse.fromEntity(customerType);
  }
}

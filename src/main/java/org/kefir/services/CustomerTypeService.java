package org.kefir.services;

import org.kefir.entities.CustomerType;
import org.kefir.repositories.CustomerTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
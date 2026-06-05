package com.kefir.services;

import com.kefir.entities.CustomerType;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
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

  public List<CustomerTypeResponse> getAllWithResponse() {
    return customerTypeRepository.findAll().stream().map(CustomerTypeResponse::fromEntity).toList();
  }

  public CustomerTypeResponse getByIdWithResponse(Integer id) {
    CustomerType customerType =
        customerTypeRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));

    return CustomerTypeResponse.fromEntity(customerType);
  }

  public CustomerType getByName(com.kefir.enums.CustomerType customerType){
    return
            customerTypeRepository
                    .findByNameIgnoreCase(customerType.name())
                    .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));

  }
}

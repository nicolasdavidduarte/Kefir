package com.kefir.services.customer;

import com.kefir.entities.Customer;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.persistence.repositories.CustomerRepository;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.utils.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerQueryService {

  private final CustomerRepository customerRepository;

  public CustomerQueryService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Transactional(readOnly = true)
  public PageResponse<CustomerResponse> getAllWithResponse(Pageable pageable) {
    Page<Customer> page = customerRepository.findAllByOrderByIdAsc(pageable);
    return PageResponse.from(page, CustomerResponse::fromEntity);
  }

  @Transactional(readOnly = true)
  public Customer getById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public CustomerResponse getByIdWithResponse(Long id) {
    return customerRepository
        .findByIdWithDetails(id)
        .map(CustomerResponse::fromEntity)
        .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public List<CustomerResponse> getAllByFullnameOrDocumentNumber(String query) {
    return customerRepository.findByFullnameOrDocumentNumber(query).stream()
        .map(CustomerResponse::fromEntity)
        .toList();
  }
}

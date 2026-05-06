package com.kefir.services;

import com.kefir.entities.Customer;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.CustomerNotFoundException;
import com.kefir.repositories.CustomerRepository;
import com.kefir.web.DTOs.CustomerDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Customer findById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(
            () ->
                new CustomerNotFoundException(String.format("Customer with id %d not found", id)));
  }

  @Transactional
  public Customer createCustomer(CustomerDTO customerDTO) {

    Customer newCustomer = new Customer();
    String fullname = setFullname(customerDTO);

    newCustomer.setName1(customerDTO.name1());
    newCustomer.setName2(customerDTO.name2());
    newCustomer.setName3(customerDTO.name3());
    newCustomer.setLastname1(customerDTO.lastname1());
    newCustomer.setLastname2(customerDTO.lastname2());
    newCustomer.setLastname3(customerDTO.lastname3());
    newCustomer.setFullname(fullname);
    newCustomer.setPersonType(customerDTO.personType());
    newCustomer.setDocumentType(customerDTO.documentType());
    newCustomer.setDocumentNumber(customerDTO.documentNumber());
    newCustomer.setCustomerType(customerDTO.customerType());
    newCustomer.setStatus(CustomerStatus.ACTIVE.getId());
    newCustomer.setLastModificationDate(LocalDateTime.now());

    Customer customerSaved = customerRepository.saveAndFlush(newCustomer);

    log.info("Customer successfully created:{}", customerSaved);
    return customerSaved;
  }

  private String setFullname(CustomerDTO customerDTO) {
    String fullname = customerDTO.name1();
    if ((customerDTO.name2() != null)) fullname = fullname + " " + customerDTO.name2();
    if ((customerDTO.name3() != null)) fullname = fullname + " " + customerDTO.name3();
    fullname = fullname + " " + customerDTO.lastname1();
    if ((customerDTO.lastname2() != null)) fullname = fullname + " " + customerDTO.lastname2();
    if ((customerDTO.lastname3() != null)) fullname = fullname + " " + customerDTO.lastname3();
    return fullname;
  }

  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    customerRepository.delete(customer);
  }
}

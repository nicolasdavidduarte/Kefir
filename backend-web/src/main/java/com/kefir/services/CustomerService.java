package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.CustomerNotFoundException;
import com.kefir.exceptions.CustomerTypeNotValidException;
import com.kefir.repositories.CustomerRepository;
import com.kefir.repositories.CustomerTypeRepository;
import com.kefir.repositories.DocumentTypeRepository;
import com.kefir.repositories.PersonTypeRepository;
import com.kefir.web.dtos.CustomerRequest;
import com.kefir.web.dtos.CustomerResponse;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final PersonTypeRepository personTypeRepository;
  private final CustomerTypeRepository customerTypeRepository;
  private final AuxAuthService auxAuthService;

  public CustomerService(
      CustomerRepository customerRepository,
      DocumentTypeRepository documentTypeRepository,
      PersonTypeRepository personTypeRepository,
      CustomerTypeRepository customerTypeRepository,
      AuxAuthService auxAuthService) {
    this.customerRepository = customerRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.personTypeRepository = personTypeRepository;
    this.customerTypeRepository = customerTypeRepository;
    this.auxAuthService = auxAuthService;
  }

  public List<CustomerResponse> getAllWithResponse() {
    return customerRepository.findAll().stream().map(CustomerResponse::toResponse).toList();
  }

  public Customer getById(Long id) {
    return customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
  }

  public CustomerResponse getByIdWithResponse(Long id) {
    return customerRepository
        .findById(id)
        .map(CustomerResponse::toResponse)
        .orElseThrow(CustomerNotFoundException::new);
  }

  @Transactional
  public CustomerResponse createCustomer(CustomerRequest customerRequest) {

    Customer newCustomer = new Customer();
    String fullname = generateFullname(customerRequest);

    DocumentType documentType =
        documentTypeRepository.findByNameIgnoreCase(customerRequest.documentType());

    PersonType personType =
        personTypeRepository
            .findByNameIgnoreCase(customerRequest.personType())
            .orElseThrow(() -> new RuntimeException("Person type not found"));

    CustomerType customerType =
        customerTypeRepository
            .findByNameIgnoreCase(customerRequest.customerType())
            .orElseThrow(CustomerNotFoundException::new);

    if (!customerType.isEnabled()) {
      throw new CustomerTypeNotValidException();
    }

    CoreUser coreUser = auxAuthService.getUserFromAuth();

    newCustomer.setName1(customerRequest.name1());
    newCustomer.setName2(customerRequest.name2());
    newCustomer.setName3(customerRequest.name3());
    newCustomer.setLastname1(customerRequest.lastname1());
    newCustomer.setLastname2(customerRequest.lastname2());
    newCustomer.setLastname3(customerRequest.lastname3());
    newCustomer.setFullname(fullname);
    newCustomer.setPersonType(personType);
    newCustomer.setDocumentType(documentType);
    newCustomer.setDocumentNumber(customerRequest.documentNumber());
    newCustomer.setCustomerType(customerType);
    newCustomer.setStatus(CustomerStatus.PENDING);
    newCustomer.setUser(coreUser);
    newCustomer.setCreatedAt(OffsetDateTime.now());
    newCustomer.setUpdatedAt(OffsetDateTime.now());

    Customer customerSaved = customerRepository.saveAndFlush(newCustomer);

    CustomerResponse response =
        CustomerResponse.builder()
            .id(customerSaved.getId())
            .name1(customerSaved.getName1())
            .name2(customerSaved.getName2())
            .name3(customerSaved.getName3())
            .lastname1(customerSaved.getLastname1())
            .lastname2(customerSaved.getLastname2())
            .lastname3(customerSaved.getLastname3())
            .personType(customerSaved.getPersonType().getName())
            .documentType(customerSaved.getDocumentType().getName())
            .documentNumber(customerSaved.getDocumentNumber())
            .customerType(customerSaved.getCustomerType().getName())
            .status(customerSaved.getStatus().toString())
            .createdByUser(customerSaved.getUser().getUsername())
            .creationDate(customerSaved.getCreatedAt())
            .build();

    log.info("Customer successfully created:{}", customerSaved);
    return response;
  }

  private String generateFullname(CustomerRequest customerRequest) {
    StringBuilder fullname = new StringBuilder();

    fullname.append(customerRequest.name1());

    if ((customerRequest.name2() != null)) fullname.append(' ').append(customerRequest.name2());
    if ((customerRequest.name3() != null)) fullname.append(' ').append(customerRequest.name3());

    fullname.append(' ').append(customerRequest.lastname1());

    if ((customerRequest.lastname2() != null))
      fullname.append(' ').append(customerRequest.lastname2());
    if ((customerRequest.lastname3() != null))
      fullname.append(' ').append(customerRequest.lastname3());

    return fullname.toString();
  }

  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
    customerRepository.delete(customer);
  }
}

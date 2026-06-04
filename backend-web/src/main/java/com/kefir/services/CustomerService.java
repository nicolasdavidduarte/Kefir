package com.kefir.services;

import com.kefir.entities.*;
import com.kefir.enums.CustomerStatus;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.CustomerRepository;
import com.kefir.repositories.CustomerTypeRepository;
import com.kefir.repositories.DocumentTypeRepository;
import com.kefir.repositories.PersonTypeRepository;
import com.kefir.web.dtos.customer.CustomerCreationRequest;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.dtos.customer.CustomerUpdateRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final DocumentTypeRepository documentTypeRepository;
  private final PersonTypeRepository personTypeRepository;
  private final CustomerTypeRepository customerTypeRepository;
  private final AuthService authService;
  private final UserService userService;

  public CustomerService(
      CustomerRepository customerRepository,
      DocumentTypeRepository documentTypeRepository,
      PersonTypeRepository personTypeRepository,
      CustomerTypeRepository customerTypeRepository,
      AuthService authService,
      UserService userService) {
    this.customerRepository = customerRepository;
    this.documentTypeRepository = documentTypeRepository;
    this.personTypeRepository = personTypeRepository;
    this.customerTypeRepository = customerTypeRepository;
    this.authService = authService;
    this.userService = userService;
  }

  public List<CustomerResponse> getAllWithResponse() {
    return customerRepository.findAll().stream().map(CustomerResponse::fromEntity).toList();
  }

  public Customer getById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
  }

  public CustomerResponse getByIdWithResponse(Long id) {
    return customerRepository
        .findById(id)
        .map(CustomerResponse::fromEntity)
        .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
  }

  @Transactional
  public CustomerResponse create(CustomerCreationRequest request) {

    DocumentType documentType = getDocumentType(request.documentType());

    PersonType personType = getPersonType(request.personType());

    CustomerType customerType = getCustomerType(request.customerType());

    User user = userService.getById(authService.getCurrentUserId());

    Customer newCustomer =
        Customer.builder()
            .name1(request.name1())
            .name2(request.name2())
            .name3(request.name3())
            .lastname1(request.lastname1())
            .lastname2(request.lastname2())
            .lastname3(request.lastname3())
            .personType(personType)
            .documentType(documentType)
            .documentNumber(request.documentNumber())
            .customerType(customerType)
            .user(user)
            .build();

    String fullname = generateFullname(newCustomer);
    newCustomer.setFullname(fullname);

    Customer customerSaved = customerRepository.saveAndFlush(newCustomer);

    return CustomerResponse.fromEntity(customerSaved);
  }

  public CustomerResponse update(CustomerUpdateRequest request, Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

    updateIfChanged(request.name1(), customer.getName1(), customer::setName1);
    updateIfChanged(request.name2(), customer.getName2(), customer::setName2);
    updateIfChanged(request.name3(), customer.getName3(), customer::setName3);

    updateIfChanged(request.lastname1(), customer.getLastname1(), customer::setLastname1);
    updateIfChanged(request.lastname2(), customer.getLastname2(), customer::setLastname2);
    updateIfChanged(request.lastname3(), customer.getLastname3(), customer::setLastname3);

    String fullname = generateFullname(customer);
    if (!fullname.equals(customer.getFullname())) customer.setFullname(fullname);

    if (request.personType() != null) customer.setPersonType(getPersonType(request.personType()));

    if (request.customerType() != null)
      customer.setCustomerType(getCustomerType(request.customerType()));

    if (request.documentType() != null)
      customer.setDocumentType(getDocumentType(request.documentType()));

    if (request.documentNumber() != null) customer.setDocumentNumber(request.documentNumber());

    customer.setUser(userService.getById(authService.getCurrentUserId()));

    customer.setUpdatedAt(OffsetDateTime.now());

    Customer customerUpdated = customerRepository.save(customer);

    return CustomerResponse.fromEntity(customerUpdated);
  }

  private String generateFullname(Customer customer) {
    StringBuilder fullname = new StringBuilder();

    fullname.append(customer.getName1());

    if (customer.getName2() != null) {
      fullname.append(' ').append(customer.getName2());
    }

    if (customer.getName3() != null) {
      fullname.append(' ').append(customer.getName3());
    }

    fullname.append(' ').append(customer.getLastname1());

    if (customer.getLastname2() != null) {
      fullname.append(' ').append(customer.getLastname2());
    }

    if (customer.getLastname3() != null) {
      fullname.append(' ').append(customer.getLastname3());
    }

    return fullname.toString();
  }

  @Transactional
  public void delete(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
    customerRepository.delete(customer);
  }

  @Transactional
  public void activate(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
    customer.setStatus(CustomerStatus.ACTIVE);

    customerRepository.save(customer);
  }

  @Transactional
  public void deactivate(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));
    customer.setStatus(CustomerStatus.DEACTIVATED);

    customerRepository.save(customer);
  }

  private PersonType getPersonType(com.kefir.enums.PersonType personType) {
    return personTypeRepository
        .findByNameIgnoreCase(personType.name())
        .orElseThrow(() -> new RuntimeException("Person type not found"));
  }

  private DocumentType getDocumentType(com.kefir.enums.DocumentType documentType) {
    return documentTypeRepository.findByNameIgnoreCase(documentType.name());
  }

  private CustomerType getCustomerType(com.kefir.enums.CustomerType customerType) {
    CustomerType customerTypeResponse =
        customerTypeRepository
            .findByNameIgnoreCase(customerType.name())
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));

    if (!customerTypeResponse.isEnabled()) {
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID);
    }

    return customerTypeResponse;
  }

  private void updateIfChanged(String newValue, String currentValue, Consumer<String> setter) {
    if (newValue != null && !newValue.equals(currentValue)) {
      setter.accept(newValue);
    }
  }
}

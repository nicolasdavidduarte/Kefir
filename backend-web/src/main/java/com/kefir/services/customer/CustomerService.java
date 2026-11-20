package com.kefir.services.customer;

import com.kefir.entities.*;
import com.kefir.enums.CustomerStatus;
import com.kefir.enums.EntityName;
import com.kefir.enums.LogOperation;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.repositories.CustomerRepository;
import com.kefir.services.customertype.CustomerTypeService;
import com.kefir.services.documenttype.DocumentTypeService;
import com.kefir.services.operationlog.OperationLogService;
import com.kefir.services.persontype.PersonTypeService;
import com.kefir.services.user.UserService;
import com.kefir.web.dtos.customer.CustomerCreationRequest;
import com.kefir.web.dtos.customer.CustomerResponse;
import com.kefir.web.dtos.customer.CustomerUpdateRequest;
import com.kefir.web.dtos.operationLog.OperationLogCommand;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final DocumentTypeService documentTypeService;
  private final PersonTypeService personTypeService;
  private final CustomerTypeService customerTypeService;
  private final AuthService authService;
  private final UserService userService;
  private final OperationLogService operationLogService;

  public CustomerService(
      CustomerRepository customerRepository,
      DocumentTypeService documentTypeService,
      CustomerTypeService customerTypeService,
      AuthService authService,
      UserService userService,
      PersonTypeService personTypeService,
      OperationLogService operationLogService) {
    this.customerRepository = customerRepository;
    this.documentTypeService = documentTypeService;
    this.personTypeService = personTypeService;
    this.customerTypeService = customerTypeService;
    this.authService = authService;
    this.userService = userService;
    this.operationLogService = operationLogService;
  }

  @Transactional(readOnly = true)
  public List<CustomerResponse> getAllWithResponse(Pageable pageable) {
    return customerRepository.findAllByOrderByIdAsc(pageable).stream()
        .map(CustomerResponse::fromEntity)
        .toList();
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

  @Transactional
  public CustomerResponse create(CustomerCreationRequest request) {

    User user = userService.getById(authService.getCurrentUserId());

    DocumentType documentType = documentTypeService.getByName(request.documentType());

    CustomerType customerType = getCustomerType(request.customerType());

    PersonType personType = personTypeService.getByName(request.personType());

    Customer newCustomer =
        Customer.create(
            request.name1(),
            request.name2(),
            request.name3(),
            request.lastname1(),
            request.lastname2(),
            request.lastname3(),
            personType,
            documentType,
            request.documentNumber(),
            customerType,
            request.email(),
            user);

    customerRepository.save(newCustomer);

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.CREATION,
            EntityName.CUSTOMER,
            newCustomer.getId(),
            "Customer successfully created",
            user));

    return CustomerResponse.fromEntity(newCustomer);
  }

  @Transactional
  public CustomerResponse update(CustomerUpdateRequest request, Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

    User user = userService.getById(authService.getCurrentUserId());

    PersonType personType = personTypeService.getByName(request.personType());

    DocumentType documentType = documentTypeService.getByName(request.documentType());

    CustomerType customerType = getCustomerType(request.customerType());

    customer.update(
        request.name1(),
        request.name2(),
        request.name3(),
        request.lastname1(),
        request.lastname2(),
        request.lastname3(),
        personType,
        documentType,
        request.documentNumber(),
        customerType,
        request.email(),
        user);

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.UPDATE,
            EntityName.CUSTOMER,
            customer.getId(),
            "Customer successfully updated",
            user));

    return CustomerResponse.fromEntity(customer);
  }

  @Transactional
  public CustomerResponse activate(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

    User user = userService.getById(authService.getCurrentUserId());

    customer.activate(user);

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.ACTIVATE,
            EntityName.CUSTOMER,
            customer.getId(),
            "Customer successfully activated",
            user));

    return CustomerResponse.fromEntity(customer);
  }

  @Transactional
  public CustomerResponse deactivate(Long id) {
    Customer customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_NOT_FOUND));

    if (customer.getStatus() != CustomerStatus.ACTIVE)
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID);

    User user = userService.getById(authService.getCurrentUserId());

    customer.deactivate(user);

    operationLogService.log(
        new OperationLogCommand(
            LogOperation.DEACTIVATION,
            EntityName.CUSTOMER,
            customer.getId(),
            "Customer successfully deactivated",
            user));

    return CustomerResponse.fromEntity(customer);
  }

  private CustomerType getCustomerType(com.kefir.enums.CustomerType customerType) {
    CustomerType customerTypeResponse = customerTypeService.getByName(customerType);

    if (!customerTypeResponse.isEnabled()) {
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID);
    }

    return customerTypeResponse;
  }
}

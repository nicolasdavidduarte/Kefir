package com.kefir.services.customer;

import com.kefir.entities.Account;
import com.kefir.entities.Customer;
import com.kefir.entities.CustomerType;
import com.kefir.entities.DocumentType;
import com.kefir.entities.Loan;
import com.kefir.entities.PersonType;
import com.kefir.entities.User;
import com.kefir.enums.AccountStatus;
import com.kefir.enums.CustomerStatus;
import com.kefir.enums.EntityName;
import com.kefir.enums.LoanStatus;
import com.kefir.enums.LogOperation;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthService;
import com.kefir.persistence.repositories.AccountRepository;
import com.kefir.persistence.repositories.CustomerRepository;
import com.kefir.persistence.repositories.LoanRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerCommandService {

  private final CustomerRepository customerRepository;
  private final DocumentTypeService documentTypeService;
  private final PersonTypeService personTypeService;
  private final CustomerTypeService customerTypeService;
  private final AuthService authService;
  private final UserService userService;
  private final OperationLogService operationLogService;
  private final AccountRepository accountRepository;
  private final LoanRepository loanRepository;

  public CustomerCommandService(
      CustomerRepository customerRepository,
      DocumentTypeService documentTypeService,
      CustomerTypeService customerTypeService,
      AuthService authService,
      UserService userService,
      PersonTypeService personTypeService,
      OperationLogService operationLogService,
      AccountRepository accountRepository,
      LoanRepository loanRepository) {
    this.customerRepository = customerRepository;
    this.documentTypeService = documentTypeService;
    this.personTypeService = personTypeService;
    this.customerTypeService = customerTypeService;
    this.authService = authService;
    this.userService = userService;
    this.operationLogService = operationLogService;
    this.accountRepository = accountRepository;
    this.loanRepository = loanRepository;
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

    PersonType personType =
        request.personType() != null ? personTypeService.getByName(request.personType()) : null;

    DocumentType documentType =
        request.documentType() != null
            ? documentTypeService.getByName(request.documentType())
            : null;

    CustomerType customerType =
        request.customerType() != null ? getCustomerType(request.customerType()) : null;

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

    List<Loan> loans =
        loanRepository.findAllByCustomerIdAndStatus(customer.getId(), LoanStatus.ACTIVE);

    if (!loans.isEmpty()) {
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID_FOR_DEACTIVATION_VALID_LOANS);
    }

    List<Account> accounts =
        accountRepository.findAllByCustomerIdAndStatus(customer.getId(), AccountStatus.OPENED);

    if (!accounts.isEmpty()) {
      throw new ApiException(ErrorCode.CUSTOMER_NOT_VALID_FOR_DEACTIVATION_ACCOUNTS_OPENED);
    }

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

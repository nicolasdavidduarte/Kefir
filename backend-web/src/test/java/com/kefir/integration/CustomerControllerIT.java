package com.kefir.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.kefir.entities.*;
import com.kefir.enums.CustomerStatus;
import com.kefir.enums.EntityName;
import com.kefir.enums.LogOperation;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthenticatedUser;
import com.kefir.repositories.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

class CustomerControllerIT extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private PersonTypeRepository personTypeRepository;

  @Autowired private DocumentTypeRepository documentTypeRepository;

  @Autowired private CustomerTypeRepository customerTypeRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private OperationLogRepository operationLogRepository;

  @BeforeEach
  public void setup() {

    AuthenticatedUser principal = new AuthenticatedUser(1, "admin");

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
            principal, null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    SecurityContextHolder.getContext().setAuthentication(authToken);
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void createCustomerSuccessfully() throws Exception {

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/customer/create-customer-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    MvcResult result =
        mockMvc
            .perform(
                post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andReturn();

    String response = result.getResponse().getContentAsString();
    long customerId = ((Number) JsonPath.read(response, "$.id")).longValue();

    Customer customer = customerRepository.findById(customerId).orElseThrow();
    assertThat(customer.getId()).isNotNull();

    OperationLog operationLog =
        operationLogRepository
            .findByEntityAndEntityIdAndOperation(
                EntityName.CUSTOMER.name(), customerId, LogOperation.CREATION.name())
            .orElseThrow();
    assertThat(operationLog.getComments()).isEqualTo("Customer successfully created");
  }

  @Test
  void createCustomerFailWhenPayloadIsInvalid() throws Exception {

    String invalidRequestBody = "{}";

    mockMvc
        .perform(
            post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestBody))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void createCustomerFailWhenUserIsUnauthorized() throws Exception {
    AuthenticatedUser unauthorizedUser = new AuthenticatedUser(2, "regular_user");
    UsernamePasswordAuthenticationToken lowPrivilegeToken =
        new UsernamePasswordAuthenticationToken(
            unauthorizedUser, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

    SecurityContextHolder.getContext().setAuthentication(lowPrivilegeToken);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/customer/create-customer-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @Test
  void createCustomerFailWhenDuplicated() throws Exception {
    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/customer/create-customer-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isConflict());
  }

  @Test
  void getAllCustomersSuccessfully() throws Exception {
    createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);
    createTestCustomer(2, "123456789", CustomerStatus.ACTIVE);

    mockMvc
        .perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        // First customer data
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name1").value("John"))
        .andExpect(jsonPath("$[0].lastname1").value("Doe"))
        .andExpect(jsonPath("$[0].fullname").value("John Doe"))
        .andExpect(jsonPath("$[0].personType").value("NATURAL"))
        .andExpect(jsonPath("$[0].documentType").value("DNI"))
        .andExpect(jsonPath("$[0].documentNumber").value("123456788"))
        .andExpect(jsonPath("$[0].customerType").value("RETAIL"))
        .andExpect(jsonPath("$[0].status").value("ACTIVE"))
        .andExpect(jsonPath("$[0].createdBy").value("admin"))
        .andExpect(jsonPath("$[0].createdAt").exists())
        .andExpect(jsonPath("$[0].updatedBy").value("admin"))
        .andExpect(jsonPath("$[0].updatedAt").exists())
        // Second customer data
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name1").value("John"))
        .andExpect(jsonPath("$[1].lastname1").value("Doe"))
        .andExpect(jsonPath("$[1].fullname").value("John Doe"))
        .andExpect(jsonPath("$[1].personType").value("NATURAL"))
        .andExpect(jsonPath("$[1].documentType").value("PASSPORT"))
        .andExpect(jsonPath("$[1].documentNumber").value("123456789"))
        .andExpect(jsonPath("$[1].customerType").value("RETAIL"))
        .andExpect(jsonPath("$[1].status").value("ACTIVE"))
        .andExpect(jsonPath("$[1].createdBy").value("admin"))
        .andExpect(jsonPath("$[1].createdAt").exists())
        .andExpect(jsonPath("$[1].updatedBy").value("admin"))
        .andExpect(jsonPath("$[1].updatedAt").exists());
  }

  @Test
  void getAllCustomersReturnsEmptyListWhenDatabaseIsEmpty() throws Exception {
    mockMvc
        .perform(get("/api/customers").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));
  }

  @Test
  void getCustomerByIdSuccessfully() throws Exception {
    Customer customer = createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);

    mockMvc
        .perform(get("/api/customers/" + customer.getId()).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        // First customer data
        .andExpect(jsonPath("id").value(1))
        .andExpect(jsonPath("name1").value("John"))
        .andExpect(jsonPath("lastname1").value("Doe"))
        .andExpect(jsonPath("fullname").value("John Doe"))
        .andExpect(jsonPath("personType").value("NATURAL"))
        .andExpect(jsonPath("documentType").value("DNI"))
        .andExpect(jsonPath("documentNumber").value("123456788"))
        .andExpect(jsonPath("customerType").value("RETAIL"))
        .andExpect(jsonPath("status").value("ACTIVE"))
        .andExpect(jsonPath("createdBy").value("admin"))
        .andExpect(jsonPath("createdAt").exists())
        .andExpect(jsonPath("updatedBy").value("admin"))
        .andExpect(jsonPath("updatedAt").exists());
  }

  @Test
  void getCustomerByIdFailWhenNotFound() throws Exception {
    mockMvc
        .perform(get("/api/customers/99").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void updateCustomerSuccessfully() throws Exception {
    Customer customer = createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);

    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/customer/update-customer-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(
            patch("/api/customers/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andDo(print())
        .andExpect(status().isOk())
        // Updated record
        .andExpect(jsonPath("id").value(1))
        .andExpect(jsonPath("name1").value("Mary"))
        .andExpect(jsonPath("name2").value("Jane"))
        .andExpect(jsonPath("lastname1").value("Sue"))
        .andExpect(jsonPath("fullname").value("Mary Jane Sue"))
        .andExpect(jsonPath("personType").value("NATURAL"))
        .andExpect(jsonPath("documentType").value("PASSPORT"))
        .andExpect(jsonPath("documentNumber").value("34555654"))
        .andExpect(jsonPath("customerType").value("CORPORATE"))
        .andExpect(jsonPath("status").value("ACTIVE"))
        .andExpect(jsonPath("createdBy").value("admin"))
        .andExpect(jsonPath("createdAt").exists())
        .andExpect(jsonPath("updatedBy").exists())
        .andExpect(jsonPath("updatedAt").exists());

    OperationLog operationLog =
        operationLogRepository
            .findByEntityAndEntityIdAndOperation(
                EntityName.CUSTOMER.name(), customer.getId(), LogOperation.UPDATE.name())
            .orElseThrow();
    assertThat(operationLog.getComments()).isEqualTo("Customer successfully updated");
  }

  @Test
  void updateCustomerFailWhenIdNotFound() throws Exception {
    String requestBody;
    try {
      requestBody =
          new ClassPathResource("requests/customer/update-customer-success.json")
              .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("File not found or unreadable", e);
    }

    mockMvc
        .perform(
            patch("/api/customers/2").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void activateCustomerStatusSuccessfully() throws Exception {
    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.PENDING);

    mockMvc
        .perform(post("/api/customers/" + customer.getId() + "/status/activate"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value("ACTIVE"));

    OperationLog operationLog =
        operationLogRepository
            .findByEntityAndEntityIdAndOperation(
                EntityName.CUSTOMER.name(), customer.getId(), LogOperation.ACTIVATE.name())
            .orElseThrow();
    assertThat(operationLog.getComments()).isEqualTo("Customer successfully activated");
  }

  @Test
  void activateCustomerFailWhenIdNotFound() throws Exception {
    mockMvc
        .perform(post("/api/customers/99/status/activate"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @ParameterizedTest
  @EnumSource(
      value = CustomerStatus.class,
      names = {"PENDING"},
      mode = EnumSource.Mode.EXCLUDE)
  void activateCustomerFailsWhenStatusIsNotValid(CustomerStatus status) throws Exception {
    Customer customer = createTestCustomer(1, "123456789", status);

    mockMvc
        .perform(post("/api/customers/" + customer.getId() + "/status/activate"))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("message").value("Customer is not valid"));
  }

  @Test
  void deactivateCustomerSuccessfully() throws Exception {
    Customer customer = createTestCustomer(1, "123456789", CustomerStatus.ACTIVE);

    mockMvc
        .perform(post("/api/customers/" + customer.getId() + "/status/deactivate"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value("DEACTIVATED"));

    OperationLog operationLog =
        operationLogRepository
            .findByEntityAndEntityIdAndOperation(
                EntityName.CUSTOMER.name(), customer.getId(), LogOperation.DEACTIVATION.name())
            .orElseThrow();
    assertThat(operationLog.getComments()).isEqualTo("Customer successfully deactivated");
  }

  @ParameterizedTest
  @EnumSource(
      value = CustomerStatus.class,
      names = {"ACTIVE"},
      mode = EnumSource.Mode.EXCLUDE)
  void deactivateCustomerFailsWhenStatusIsNotValid(CustomerStatus status) throws Exception {
    Customer customer = createTestCustomer(1, "123456789", status);

    mockMvc
        .perform(post("/api/customers/" + customer.getId() + "/status/deactivate"))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("message").value("Customer is not valid"));
  }

  @Test
  void deactivateCustomerFailWhenIdNotFound() throws Exception {
    mockMvc
        .perform(post("/api/customers/99/status/deactivate"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void getAllCustomersWithPagination() throws Exception {
    createTestCustomer(1, "123456786", CustomerStatus.ACTIVE);
    createTestCustomer(2, "123456787", CustomerStatus.ACTIVE);
    createTestCustomer(1, "123456788", CustomerStatus.ACTIVE);
    createTestCustomer(2, "123456789", CustomerStatus.ACTIVE);

    mockMvc
        .perform(get("/api/customers?page=2&size=2").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        // First customer data
        .andExpect(jsonPath("$[0].id").value(3))
        // Second customer data
        .andExpect(jsonPath("$[1].id").value(4));
  }

  @ParameterizedTest
  @CsvSource({
    "'page=0&size=2', 'Page must be greater than 0'",
    "'page=1&size=-3', 'Size must be greater than 0'",
    "'page=1&size=23', 'Size must not exceed 20'",
    "'page=2', 'Page and size must be provided together'",
    "'size=23', 'Page and size must be provided together'"
  })
  void getAllCustomersWithInvalidPaginationParameters_returnsBadRequest(
      String queryParams, String expectedMessage) throws Exception {
    mockMvc
        .perform(get("/api/customers?" + queryParams))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(expectedMessage));
  }

  Customer createTestCustomer(
      Integer documentTypeId, String documentNumber, CustomerStatus status) {
    PersonType personType =
        personTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND));
    DocumentType documentType =
        documentTypeRepository
            .findById(documentTypeId)
            .orElseThrow(() -> new ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND));
    CustomerType customerType =
        customerTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));
    User user =
        userRepository.findById(2).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    OffsetDateTime now = OffsetDateTime.now();

    return customerRepository.save(
        Customer.builder()
            .name1("John")
            .lastname1("Doe")
            .fullname("John Doe")
            .personType(personType)
            .documentType(documentType)
            .documentNumber(documentNumber)
            .customerType(customerType)
            .status(status)
            .createdBy(user)
            .createdAt(now)
            .updatedBy(user)
            .updatedAt(now)
            .build());
  }
}

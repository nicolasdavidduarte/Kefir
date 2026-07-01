package com.kefir.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kefir.entities.*;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.infrastructure.security.AuthenticatedUser;
import com.kefir.repositories.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

class CustomerControllerIT extends IntegrationTestBase {

  @Autowired private MockMvc mockMvc;

  @Autowired private CustomerRepository customerRepository;

  @Autowired private PersonTypeRepository personTypeRepository;

  @Autowired private DocumentTypeRepository documentTypeRepository;

  @Autowired private CustomerTypeRepository customerTypeRepository;

  @Autowired private UserRepository userRepository;

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

    mockMvc
        .perform(
            post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists());
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
    createTestCustomer(1, "123456788");
    createTestCustomer(2, "123456789");

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
        .andExpect(jsonPath("$[0].status").value("PENDING"))
        .andExpect(jsonPath("$[0].createdByUser").value("admin"))
        .andExpect(jsonPath("$[0].creationDate").exists())
        // Second customer data
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name1").value("John"))
        .andExpect(jsonPath("$[1].lastname1").value("Doe"))
        .andExpect(jsonPath("$[1].fullname").value("John Doe"))
        .andExpect(jsonPath("$[1].personType").value("NATURAL"))
        .andExpect(jsonPath("$[1].documentType").value("PASSPORT"))
        .andExpect(jsonPath("$[1].documentNumber").value("123456789"))
        .andExpect(jsonPath("$[1].customerType").value("RETAIL"))
        .andExpect(jsonPath("$[1].status").value("PENDING"))
        .andExpect(jsonPath("$[1].createdByUser").value("admin"))
        .andExpect(jsonPath("$[1].creationDate").exists());
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
    createTestCustomer(1, "123456788");

    mockMvc
        .perform(get("/api/customers/1").contentType(MediaType.APPLICATION_JSON))
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
        .andExpect(jsonPath("status").value("PENDING"))
        .andExpect(jsonPath("createdByUser").value("admin"))
        .andExpect(jsonPath("creationDate").exists());
  }

  @Test
  void getCustomerByIdFailWhenNotFound() throws Exception {
    mockMvc
        .perform(get("/api/customers/1").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void updateCustomerSuccessfully() throws Exception {
    createTestCustomer(1, "123456788");

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
            patch("/api/customers/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
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
        .andExpect(jsonPath("status").value("PENDING"))
        .andExpect(jsonPath("createdByUser").value("admin"))
        .andExpect(jsonPath("creationDate").exists())
        .andExpect(jsonPath("updateDate").exists());
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
  void deleteCustomerSuccessfully() throws Exception {
    createTestCustomer(1, "123456789");
    mockMvc.perform(delete("/api/customers/1")).andDo(print()).andExpect(status().isOk());
  }

  @Test
  void deleteCustomerFailWhenIdNotFound() throws Exception {
    mockMvc.perform(delete("/api/customers/1")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  void activateCustomerStatusSuccessfully() throws Exception {
    createTestCustomer(1, "123456789");

    mockMvc
        .perform(post("/api/customers/1/status/activate"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value("ACTIVE"));
  }

  @Test
  void activateCustomerFailWhenIdNotFound() throws Exception {
    mockMvc
        .perform(post("/api/customers/1/status/activate"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void deactivateCustomerSuccessfully() throws Exception {
    createTestCustomer(1, "123456789");

    mockMvc
        .perform(post("/api/customers/1/status/deactivate"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("status").value("DEACTIVATED"));
  }

  @Test
  void deactivateCustomerFailWhenIdNotFound() throws Exception {
    mockMvc
        .perform(post("/api/customers/1/status/activate"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  void createTestCustomer(Integer id, String documentNumber) {
    PersonType personType =
        personTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND));
    DocumentType documentType =
        documentTypeRepository
            .findById(id)
            .orElseThrow(() -> new ApiException(ErrorCode.DOCUMENT_TYPE_NOT_FOUND));
    CustomerType customerType =
        customerTypeRepository
            .findById(1)
            .orElseThrow(() -> new ApiException(ErrorCode.CUSTOMER_TYPE_NOT_FOUND));
    User user =
        userRepository.findById(1).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

    customerRepository.save(
        Customer.builder()
            .name1("John")
            .lastname1("Doe")
            .fullname("John Doe")
            .personType(personType)
            .documentType(documentType)
            .documentNumber(documentNumber)
            .customerType(customerType)
            .user(user)
            .build());
  }
}

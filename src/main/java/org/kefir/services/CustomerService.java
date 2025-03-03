package org.kefir.services;

import java.util.List;
import java.util.Optional;
import org.kefir.DTOs.CustomerDTO;
import org.kefir.entities.Customer;
import org.kefir.entities.Loan;
import org.kefir.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public List<Customer> findAll() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(Long id) {
    return customerRepository.findById(id);
  }

  @Transactional
  public Customer createCustomer(CustomerDTO customerDTO) {

    Customer newCustomer = new Customer();

    newCustomer.setName(customerDTO.getName());
    newCustomer.setFirstName1(customerDTO.getFirstName1());
    newCustomer.setFirstName2(customerDTO.getFirstName2());
    newCustomer.setLastName1(customerDTO.getLastName1());
    newCustomer.setLastName2(customerDTO.getLastName2());
    newCustomer.setNameShort(customerDTO.getNameShort());
    newCustomer.setPersonType(customerDTO.getPersonType());
    newCustomer.setCustomerType(customerDTO.getCustomerType());
    newCustomer.setStatus(customerDTO.getStatus());
    newCustomer.setLastModificationDate(customerDTO.getLastModificationDate());
    newCustomer.setCoreUser(customerDTO.getCoreUser());

    return customerRepository.save(newCustomer);
  }

  @Transactional
  public void deleteCustomer(Long id) {
    Customer customer =
            customerRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    customerRepository.delete(customer);
  }
}

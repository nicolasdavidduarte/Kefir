package com.kefir.services;

import com.kefir.entities.PersonType;
import com.kefir.repositories.PersonTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonTypeService {

  private final PersonTypeRepository personTypeRepository;

  public PersonTypeService(PersonTypeRepository personTypeRepository) {
    this.personTypeRepository = personTypeRepository;
  }

  public List<PersonType> findAll() {
    return personTypeRepository.findAll();
  }

  public Optional<PersonType> findById(Integer id) {
    return personTypeRepository.findById(id);
  }
}

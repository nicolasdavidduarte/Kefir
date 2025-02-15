package org.kefir.services;

import org.kefir.entities.PersonType;
import org.kefir.repositories.PersonTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonTypeService {

    private final PersonTypeRepository personTypeRepository;

    public PersonTypeService(PersonTypeRepository personTypeRepository) {
        this.personTypeRepository = personTypeRepository;
    }

    public List<PersonType> findAll() {
        return personTypeRepository.findAll();
    }

    public Optional<PersonType> findById(Long id) {
        return personTypeRepository.findById(id);
    }


}
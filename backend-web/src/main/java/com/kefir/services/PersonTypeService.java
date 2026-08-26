package com.kefir.services;

import com.kefir.entities.PersonType;
import com.kefir.exceptions.ApiException;
import com.kefir.exceptions.ErrorCode;
import com.kefir.repositories.PersonTypeRepository;
import com.kefir.web.dtos.personType.PersonTypeResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonTypeService {

  private final PersonTypeRepository personTypeRepository;

  public PersonTypeService(PersonTypeRepository personTypeRepository) {
    this.personTypeRepository = personTypeRepository;
  }

  public List<PersonTypeResponse> getAllWithResponse() {
    return personTypeRepository.findAllByOrderByIdAsc().stream()
        .map(PersonTypeResponse::fromEntity)
        .toList();
  }

  public PersonTypeResponse getByIdWithResponse(Integer id) {
    PersonType personType =
        personTypeRepository
            .findByIdWithDetails(id)
            .orElseThrow(() -> new ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND));

    return PersonTypeResponse.fromEntity(personType);
  }

  public PersonType getByName(com.kefir.enums.PersonType personType) {
    return personTypeRepository
        .findByNameIgnoreCase(personType.name())
        .orElseThrow(() -> new ApiException(ErrorCode.PERSON_TYPE_NOT_FOUND));
  }
}

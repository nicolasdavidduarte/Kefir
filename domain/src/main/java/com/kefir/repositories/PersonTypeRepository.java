package com.kefir.repositories;

import com.kefir.entities.PersonType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Integer> {
  Optional<PersonType> findByNameIgnoreCase(String name);
}

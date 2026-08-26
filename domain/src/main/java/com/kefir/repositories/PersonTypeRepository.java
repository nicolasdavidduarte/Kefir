package com.kefir.repositories;

import com.kefir.entities.PersonType;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Integer> {
  Optional<PersonType> findByNameIgnoreCase(String name);

  @Override
  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @NonNull Optional<PersonType> findById(@NonNull Integer Id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<PersonType> findAllByOrderByIdAsc();
}

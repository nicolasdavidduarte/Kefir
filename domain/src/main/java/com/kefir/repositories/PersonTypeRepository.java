package com.kefir.repositories;

import com.kefir.entities.PersonType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonTypeRepository extends JpaRepository<PersonType, Integer> {
  Optional<PersonType> findByNameIgnoreCase(String name);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @Query(value = "select pt from PersonType pt where pt.id = :id")
  Optional<PersonType> findByIdWithDetails(@Param("id") Integer id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<PersonType> findAllByOrderByIdAsc();
}

package com.kefir.repositories;

import com.kefir.entities.CustomerType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Integer> {
  Optional<CustomerType> findByNameIgnoreCase(String name);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @Query(value = "select ct from CustomerType ct where ct.id = :id")
  Optional<CustomerType> findByIdWithDetails(@Param("id") Integer id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<CustomerType> findAllByOrderByIdAsc();
}

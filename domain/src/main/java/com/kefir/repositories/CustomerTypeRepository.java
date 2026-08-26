package com.kefir.repositories;

import com.kefir.entities.CustomerType;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Integer> {
  Optional<CustomerType> findByNameIgnoreCase(String name);

  @Override
  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  @NonNull Optional<CustomerType> findById(@NonNull Integer Id);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy"})
  List<CustomerType> findAllByOrderByIdAsc();
}

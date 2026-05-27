package com.kefir.repositories;

import com.kefir.entities.CustomerType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Integer> {
  Optional<CustomerType> findByNameIgnoreCase(String name);
}

package com.kefir.repositories;

import com.kefir.entities.Role;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  @Query(
"""
    SELECT r
    FROM Role r
    WHERE UPPER(r.name) IN :names
""")
  Set<Role> findByNamesIgnoreCase(@Param("names") List<String> names);
}

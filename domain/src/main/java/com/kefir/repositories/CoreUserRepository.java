package com.kefir.repositories;

import com.kefir.entities.CoreUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreUserRepository extends JpaRepository<CoreUser, Long> {

  Optional<CoreUser> findByUsername(String username);

  @Query("select cu.id from CoreUser cu where cu.username = :username")
  Optional<Integer> findIdByUsername(@Param("username") String username);
}

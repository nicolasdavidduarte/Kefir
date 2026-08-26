package com.kefir.repositories;

import com.kefir.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @EntityGraph(attributePaths = {"createdBy", "updatedBy", "roles"})
  @Query("SELECT u FROM User u WHERE u.id=:id")
  Optional<User> findByIdWithDetails(@Param("id") Integer id);

  Optional<User> findByUsername(String username);

  @EntityGraph(attributePaths = {"createdBy", "updatedBy", "roles"})
  @Query("SELECT u FROM User u WHERE u.id <> 1 ORDER BY u.id")
  List<User> findAllAvailableUsersOrderById();
}

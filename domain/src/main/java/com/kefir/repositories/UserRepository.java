package com.kefir.repositories;

import com.kefir.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  @Query("SELECT u FROM User u WHERE u.id <> 1 ORDER BY u.id")
  List<User> findAllAvailableUsersOrderById();
}

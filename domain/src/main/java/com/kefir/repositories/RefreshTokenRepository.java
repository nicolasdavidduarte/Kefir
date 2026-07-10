package com.kefir.repositories;

import com.kefir.entities.RefreshToken;
import com.kefir.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  @Query("SELECT r FROM RefreshToken r WHERE r.createdBy = :user")
  List<RefreshToken> findByUser(@Param("user") User user);

  @Modifying
  @Transactional
  @Query("DELETE FROM RefreshToken r WHERE r.createdBy = :user")
  void deleteByUser(@Param("user") User user);
}

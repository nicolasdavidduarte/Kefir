package com.kefir.repositories;

import com.kefir.entities.RefreshToken;
import com.kefir.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findByUser(User user);

  void deleteByUser(User user);
}

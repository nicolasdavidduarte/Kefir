package com.kefir.repositories;

import com.kefir.entities.CoreUser;
import com.kefir.entities.RefreshToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  List<RefreshToken> findByUser(CoreUser user);

  void deleteByUser(CoreUser user);
}

package com.kefir.repositories;

import com.kefir.model.IdempotentRequest;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotentRequestRepository extends JpaRepository<IdempotentRequest, String> {
  Optional<IdempotentRequest> findByIdempotencyKey(String key);
}

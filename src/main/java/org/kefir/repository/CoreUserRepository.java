package org.kefir.repository;

import org.kefir.entity.CoreUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemUserRepository extends JpaRepository<CoreUser, Long> {
}
package org.kefir.repository;

import org.kefir.entity.Testo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestoRepository extends JpaRepository<Testo, Long> {
}
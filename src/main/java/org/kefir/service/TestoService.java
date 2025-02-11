package org.kefir.service;

import org.kefir.entity.Testo;
import org.kefir.repository.TestoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestoService {
    
    private final TestoRepository testoRepository;

    public TestoService(TestoRepository testoRepository) {
        this.testoRepository = testoRepository;
    }

    public List<Testo> findAll() {
        return testoRepository.findAll();
    }

    public Optional<Testo> findById(Long id) {
        return testoRepository.findById(id);
    }


}
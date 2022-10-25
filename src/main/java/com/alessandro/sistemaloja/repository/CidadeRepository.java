package com.alessandro.sistemaloja.repository;

import com.alessandro.sistemaloja.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}

package com.alessandro.sistemaloja.repository;

import com.alessandro.sistemaloja.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}

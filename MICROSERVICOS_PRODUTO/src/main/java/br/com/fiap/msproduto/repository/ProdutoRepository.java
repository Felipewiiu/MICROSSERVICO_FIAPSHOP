package br.com.fiap.msproduto.repository;

import br.com.fiap.msproduto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}

package br.com.fiap.msproduto.service;

import br.com.fiap.msproduto.model.Produto;
import br.com.fiap.msproduto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public List<Produto>listarProduto (){
        return produtoRepository.findAll();
    }

    public Produto cadastrarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public ResponseEntity<?> listarUmProduto(Integer produtoId) {
    Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if(produto != null) {
            return ResponseEntity.ok(produto);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

    }

    public Produto atualizarUmProduto(Integer produtoId, Produto novoProduto) {
        Produto produtoExistente = produtoRepository.findById(produtoId).orElse(null);

        if(produtoExistente != null) {
            produtoExistente.setNome(novoProduto.getNome());
            produtoExistente.setDescricao(novoProduto.getDescricao());
            produtoExistente.setPreco(novoProduto.getPreco());
            produtoExistente.setQuantidade_estoque(novoProduto.getQuantidade_estoque());
            return produtoRepository.save(produtoExistente);
        } else {
            throw new NoSuchElementException("Produto não encontrado!");
        }
    }

    public void excluirUmProduto(Integer produtoId) {
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if(produto != null) {
            produtoRepository.delete(produto);
        } else {
            throw  new NoSuchElementException("Produto não encontrado!");
        }
    }

    public Produto atualizarEstoque(Integer produtoId, int quantidade){
        Produto produto = produtoRepository.findById(produtoId).orElse(null);

        if(produto != null) {
            produto.setQuantidade_estoque(produto.getQuantidade_estoque()-quantidade);
            produtoRepository.save(produto);
        }

        return produto;
    }
}

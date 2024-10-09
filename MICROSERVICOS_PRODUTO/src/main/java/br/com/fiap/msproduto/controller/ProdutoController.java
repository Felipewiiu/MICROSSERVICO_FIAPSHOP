package br.com.fiap.msproduto.controller;

import br.com.fiap.msproduto.model.Produto;
import br.com.fiap.msproduto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoService.listarProduto();
    }

    @PostMapping
    public Produto cadastrarProduto(@RequestBody Produto produto){
        return produtoService.cadastrarProduto(produto);
    }
    @GetMapping("/{produtoId}")
    public ResponseEntity<?> listarUmProdutos(@PathVariable Integer produtoId){
        return produtoService.listarUmProduto(produtoId);
    }

    @PutMapping("/atualizar/{produtoId}")
    public Produto atualizarProduto(@PathVariable Integer produtoId, @RequestBody Produto produto){
        return  produtoService.atualizarUmProduto(produtoId, produto);
    }

    @PutMapping("/atualizar/estoque/{produtoId}/{quantidade}")
    public ResponseEntity<Produto> atualizarEstoque(@PathVariable Integer produtoId, @PathVariable  int quantidade){
        var produto = produtoService.atualizarEstoque(produtoId, quantidade);

        return ResponseEntity.status(HttpStatus.OK).body(produto);
    }

    @DeleteMapping("/{produtoId}")
    public void excluirProduto (@PathVariable Integer produtoId){
        produtoService.excluirUmProduto(produtoId);
    }


}

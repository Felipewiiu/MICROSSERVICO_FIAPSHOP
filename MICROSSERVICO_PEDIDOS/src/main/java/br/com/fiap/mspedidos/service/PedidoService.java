package br.com.fiap.mspedidos.service;

import br.com.fiap.mspedidos.model.ItemPedido;
import br.com.fiap.mspedidos.model.Pedido;
import br.com.fiap.mspedidos.repository.PedidoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    // Configuração de integração de microsserviço
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public Pedido criarPedido(Pedido pedido) {

        boolean produtoDisponivel = verificarDispnibilidadeDeProduto(pedido.getItensPedido());

        if(!produtoDisponivel) {
            throw new NoSuchElementException("Um ou mais produtos não estão disponíveis");
        }

        double valorTotal = calcularValorTotal(pedido.getItensPedido());
        pedido.setValorTotal(valorTotal);

        atualizarEstoqueDeProdutos(pedido.getItensPedido());

        Pedido novoPedido = pedidoRepository.save(pedido);

        return novoPedido;
    }


    private boolean verificarDispnibilidadeDeProduto(List<ItemPedido> itensPedido) {

        for(ItemPedido itemPedido : itensPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produto/{produtoId}", String.class, idProduto
            );

            if( response.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new NoSuchElementException("Produto não encontrado");
            }else{
                try {
                    JsonNode produtoJson = objectMapper.readTree(response.getBody());
                    int quantidadeDeEstoque = produtoJson.get("quantidade_estoque").asInt();

                    if(quantidadeDeEstoque < quantidade){
                        return false;
                    }
                }catch (IOException e){
                    //implementar depois
                }
            }
        }
        return true;
    }

    private double calcularValorTotal(List<ItemPedido> itensPedido) {
        double valorTotal = 0.0;

        for(ItemPedido itemPedido : itensPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:8080/api/produto/{produtoId}", String.class, idProduto
            );

            if( response.getStatusCode() == HttpStatus.OK){
                try {
                    JsonNode produtoJson = objectMapper.readTree(response.getBody());
                    double preco = produtoJson.get("preco").asDouble();
                    valorTotal += preco * quantidade;

                }catch (IOException e){
                    /// implementar depois
                }
            }

        }
        return valorTotal;
    }

    private void atualizarEstoqueDeProdutos(List<ItemPedido> itensPedido) {

        for(ItemPedido itemPedido : itensPedido) {
            Integer idProduto = itemPedido.getIdProduto();
            int quantidade = itemPedido.getQuantidade();

            restTemplate.put(
                    "http://localhost:8080/api/produtos/atualizar/estoque/{produtoId}/{quantidade}",
                    null,
                    idProduto,
                    quantidade
            );
        }
    }

}

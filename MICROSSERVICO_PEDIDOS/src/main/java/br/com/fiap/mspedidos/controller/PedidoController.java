package br.com.fiap.mspedidos.controller;


import br.com.fiap.mspedidos.model.Pedido;
import br.com.fiap.mspedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> salvarPedido(@RequestBody Pedido pedido) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        }catch (NoSuchElementException e) {
            return new ResponseEntity<>("Um ou mais pedidos não estão disponível", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public List<Pedido> getPedidos() {
        return pedidoService.buscarTodosPedidos();
    }
}

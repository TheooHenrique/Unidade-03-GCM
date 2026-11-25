package com.theojamilly.gcm.controller;

import com.theojamilly.gcm.dto.ConsultarEstoqueResponse;
import com.theojamilly.gcm.service.LojaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.theojamilly.gcm.dto.CadastrarProdutoRequest;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.model.Produto;
import com.theojamilly.gcm.service.LojaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController 
@RequestMapping("/loja")
public class LojaController {
    private final LojaService lojaService;

    @PostMapping("/produto")
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody CadastrarProdutoRequest request) {
	if (request == null) {
	    throw new DadosInvalidosException("Corpo da requisição ausente.");
	}
	Produto criado = lojaService.cadastrarProduto(request.codigo(), request.preco());
	return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping("/produto/{codigo}/estoque")
    public ResponseEntity<ConsultarEstoqueResponse> consultarEstoque(@PathVariable Integer codigo) {
        Integer estoque = lojaService.consultarEstoque(codigo);
        return ResponseEntity.ok(new ConsultarEstoqueResponse(codigo, estoque));
    }
}

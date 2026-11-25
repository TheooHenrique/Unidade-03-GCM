package com.theojamilly.gcm.controller;

import com.theojamilly.gcm.dto.EntradaEstoqueRequest;
import com.theojamilly.gcm.dto.EntradaEstoqueResponse;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.service.LojaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/loja")
public class LojaController {
    private final LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @PutMapping("/produto/{codigo}/entrada")
    public ResponseEntity<EntradaEstoqueResponse> entradaEstoque(
            @PathVariable Integer codigo,
            @RequestBody EntradaEstoqueRequest request) {
        if (request == null) {
            throw new DadosInvalidosException("Corpo da requisição ausente.");
        }
        Integer novoEstoque = lojaService.entradaEstoque(codigo, request.quantidade());
        return ResponseEntity.ok(new EntradaEstoqueResponse(codigo, novoEstoque));
    }
}

package com.theojamilly.gcm.controller;

import com.theojamilly.gcm.dto.ConsultarEstoqueResponse;
import com.theojamilly.gcm.service.LojaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping("/loja")
public class LojaController {
    private final LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @GetMapping("/produto/{codigo}/estoque")
    public ResponseEntity<ConsultarEstoqueResponse> consultarEstoque(@PathVariable Integer codigo) {
        Integer estoque = lojaService.consultarEstoque(codigo);
        return ResponseEntity.ok(new ConsultarEstoqueResponse(codigo, estoque));
    }
}

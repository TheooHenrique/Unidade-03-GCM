package com.theojamilly.gcm.service;

import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SaidaEstoqueTest {
    private LojaService lojaService;

    @BeforeEach
    void setUp() {
        lojaService = new LojaService();
    }

    @Test
    void deveSubtrairQuantidadeDoEstoqueNoCasoNormal() {
        lojaService.cadastrarProduto(201, new BigDecimal("59.90"));
        lojaService.entradaEstoque(201, 20);

        Integer novoEstoque = lojaService.saidaEstoque(201, 7);

        assertEquals(13, novoEstoque);
        assertEquals(13, lojaService.consultarEstoque(201));
    }

    @Test
    void deveLancarExcecaoParaProdutoInexistente() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            lojaService.saidaEstoque(999, 5);
        });
    }

    @Test
    void devePermitirSaidaMaiorQueEstoqueEDeixarSaldoNegativo() {
        lojaService.cadastrarProduto(202, new BigDecimal("35.00"));
        lojaService.entradaEstoque(202, 10);

        Integer novoEstoque = lojaService.saidaEstoque(202, 15);

        assertEquals(-5, novoEstoque);
        assertEquals(-5, lojaService.consultarEstoque(202));
    }

    @Test
    void deveLancarExcecaoENaoAlterarEstoqueQuandoQuantidadeForInvalida() {
        lojaService.cadastrarProduto(203, new BigDecimal("40.00"));
        lojaService.entradaEstoque(203, 10);

        assertThrows(DadosInvalidosException.class, () -> lojaService.saidaEstoque(203, 0));
        assertThrows(DadosInvalidosException.class, () -> lojaService.saidaEstoque(203, -2));

        assertEquals(10, lojaService.consultarEstoque(203));
    }
}

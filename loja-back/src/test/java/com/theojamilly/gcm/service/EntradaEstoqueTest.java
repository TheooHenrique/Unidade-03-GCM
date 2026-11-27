package com.theojamilly.gcm.service;

import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntradaEstoqueTest {
    private LojaService lojaService;

    @BeforeEach
    void setUp() {
        lojaService = new LojaService();
    }

    @Test
    void deveAdicionarQuantidadeAoEstoqueNoCasoNormal() {
        lojaService.cadastrarProduto(101, new BigDecimal("49.90"));

        Integer estoqueAposPrimeiraEntrada = lojaService.entradaEstoque(101, 10);
        assertEquals(10, estoqueAposPrimeiraEntrada);
        assertEquals(10, lojaService.consultarEstoque(101));

        Integer estoqueAposSegundaEntrada = lojaService.entradaEstoque(101, 15);
        assertEquals(25, estoqueAposSegundaEntrada);
        assertEquals(25, lojaService.consultarEstoque(101));
    }

    @Test
    void deveLancarExcecaoParaProdutoInexistente() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            lojaService.entradaEstoque(999, 10);
        });
    }

    @Test
    void deveLancarExcecaoENaoAlterarEstoqueQuandoQuantidadeForInvalida() {
        lojaService.cadastrarProduto(101, new BigDecimal("49.90"));
        lojaService.entradaEstoque(101, 10);

        assertThrows(DadosInvalidosException.class, () -> lojaService.entradaEstoque(101, 0));
        assertThrows(DadosInvalidosException.class, () -> lojaService.entradaEstoque(101, -5));

        assertEquals(10, lojaService.consultarEstoque(101));
    }
}

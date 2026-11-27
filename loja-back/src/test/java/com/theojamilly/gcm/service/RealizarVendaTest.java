package com.theojamilly.gcm.service;

import com.theojamilly.gcm.dto.ItemVendaRequest;
import com.theojamilly.gcm.dto.VendaRequest;
import com.theojamilly.gcm.dto.VendaResponse;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RealizarVendaTest {
    private LojaService lojaService;

    @BeforeEach
    void setUp() {
        lojaService = new LojaService();
    }

    @Test
    void deveBaixarEstoqueDeTodosOsItensECalcularTotalCorretamente() {
        lojaService.cadastrarProduto(301, new BigDecimal("50.00"));
        lojaService.entradaEstoque(301, 10);

        lojaService.cadastrarProduto(302, new BigDecimal("30.50"));
        lojaService.entradaEstoque(302, 8);

        VendaRequest request = new VendaRequest(List.of(
                new ItemVendaRequest(301, 2), // 100.00
                new ItemVendaRequest(302, 3)  //  91.50
        ));

        VendaResponse response = lojaService.realizarVenda(request);

        assertEquals(new BigDecimal("191.50"), response.total());
        assertEquals(2, response.itens().size());
        assertEquals(8, lojaService.consultarEstoque(301));
        assertEquals(5, lojaService.consultarEstoque(302));
    }

    @Test
    void deveFalharENaoAlterarEstoqueDeNenhumItemQuandoHouverProdutoInexistente() {
        lojaService.cadastrarProduto(301, new BigDecimal("50.00"));
        lojaService.entradaEstoque(301, 10);

        VendaRequest request = new VendaRequest(List.of(
                new ItemVendaRequest(301, 2),
                new ItemVendaRequest(999, 1) // Produto inexistente
        ));

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            lojaService.realizarVenda(request);
        });

        // Garante atomicidade: o estoque do produto 301 não foi debitado
        assertEquals(10, lojaService.consultarEstoque(301));
    }

    @Test
    void deveFalharENaoAlterarEstoqueQuandoAlgumItemTiverSaldoInsuficiente() {
        lojaService.cadastrarProduto(301, new BigDecimal("50.00"));
        lojaService.entradaEstoque(301, 10);

        lojaService.cadastrarProduto(302, new BigDecimal("30.50"));
        lojaService.entradaEstoque(302, 2);

        VendaRequest request = new VendaRequest(List.of(
                new ItemVendaRequest(301, 2),
                new ItemVendaRequest(302, 5) // Estoque insuficiente (só tem 2)
        ));

        assertThrows(DadosInvalidosException.class, () -> {
            lojaService.realizarVenda(request);
        });

        assertEquals(10, lojaService.consultarEstoque(301));
        assertEquals(2, lojaService.consultarEstoque(302));
    }
}

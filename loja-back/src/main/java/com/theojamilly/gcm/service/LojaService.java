package com.theojamilly.gcm.service;

import org.springframework.stereotype.Service;
import com.theojamilly.gcm.model.Produto;
import com.theojamilly.gcm.model.itemVenda;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LojaService {
    
    private final Map<Integer, Produto> produtos = new HashMap<>();

    public Produto cadastrarProduto(Integer codigo, BigDecimal preco) {
        throw new UnsupportedOperationException("Não implementado");
    }

    public Integer consultarEstoque(Integer codigo) {
        if (codigo == null || codigo <= 0) {
            throw new DadosInvalidosException("O código do produto deve ser um inteiro positivo.");
        }

        Produto produto = produtos.get(codigo);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado com o código " + codigo + ".");
        }

        return produto.getEstoque();
    }

    public Produto entradaEstoque(Integer codigo, Integer quantidade) {
        throw new UnsupportedOperationException("Não implementado");
    }

    public Produto saidaEstoque(Integer codigo, Integer quantidade) {
        throw new UnsupportedOperationException("Não implementado");
    }

    public Map<String, Object> realizarVenda(List<itemVenda> itens) {
        throw new UnsupportedOperationException("Não implementado");
    }
}

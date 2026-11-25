package com.theojamilly.gcm.service;

import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoJaExisteException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;
import com.theojamilly.gcm.model.itemVenda;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;

import com.theojamilly.gcm.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LojaService {
    
    private final Map<Integer, Produto> produtos = new HashMap<>();

    public Produto cadastrarProduto(Integer codigo, BigDecimal preco) {
        if (codigo == null || codigo <= 0) {
	    throw new DadosInvalidosException("O código do produto deve ser um inteiro positivo.");
	}
	if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
	    throw new DadosInvalidosException("O preço do produto deve ser maior que zero.");
	}
	if (produtos.containsKey(codigo)) {
	    throw new ProdutoJaExisteException("Já existe um produto cadastrado com o código " + codigo + ".");
	}

	Produto novoProduto = new Produto(codigo, preco, 0);
	produtos.put(codigo, novoProduto);
	return novoProduto;
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

    public Integer entradaEstoque(Integer codigo, Integer quantidade) {
        if (codigo == null || codigo <= 0) {
            throw new DadosInvalidosException("O código do produto deve ser um inteiro positivo.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new DadosInvalidosException("A quantidade de entrada deve ser um inteiro positivo.");
        }

        Produto produto = produtos.get(codigo);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado com o código " + codigo + ".");
        }

        int novoEstoque = produto.getEstoque() + quantidade;
        produto.setEstoque(novoEstoque);
        return novoEstoque;
    }

    public Produto saidaEstoque(Integer codigo, Integer quantidade) {
        throw new UnsupportedOperationException("Não implementado");
    }

    public Map<String, Object> realizarVenda(List<itemVenda> itens) {
        throw new UnsupportedOperationException("Não implementado");
    }
}

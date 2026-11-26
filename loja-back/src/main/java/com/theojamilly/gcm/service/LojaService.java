package com.theojamilly.gcm.service;

import com.theojamilly.gcm.dto.ItemVendaRequest;
import com.theojamilly.gcm.dto.ItemVendaResponse;
import com.theojamilly.gcm.dto.VendaRequest;
import com.theojamilly.gcm.dto.VendaResponse;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoJaExisteException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;
import com.theojamilly.gcm.model.itemVenda;
import com.theojamilly.gcm.exception.DadosInvalidosException;
import com.theojamilly.gcm.exception.ProdutoNaoEncontradoException;

import com.theojamilly.gcm.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    public Integer saidaEstoque(Integer codigo, Integer quantidade) {
        if (codigo == null || codigo <= 0) {
            throw new DadosInvalidosException("O código do produto deve ser um inteiro positivo.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new DadosInvalidosException("A quantidade de saída deve ser um inteiro positivo.");
        }

        Produto produto = produtos.get(codigo);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException("Produto não encontrado com o código " + codigo + ".");
        }
        if (produto.getEstoque() < quantidade) {
            throw new DadosInvalidosException("Estoque insuficiente para o produto " + codigo + ".");
        }

        int novoEstoque = produto.getEstoque() - quantidade;
        produto.setEstoque(novoEstoque);
        return novoEstoque;
    }

    public Map<String, Object> realizarVenda(List<itemVenda> itens) {
        throw new UnsupportedOperationException("Não implementado");
    }
    
    public VendaResponse realizarVenda(VendaRequest request) {
        if (request == null || request.itens() == null || request.itens().isEmpty()) {
            throw new DadosInvalidosException("A lista de itens da venda não pode ser vazia.");
        }

        // 1. Agrupa quantidades por código (caso o mesmo livro apareça 2x no JSON) e valida entradas
        Map<Integer, Integer> quantidadesPorCodigo = new LinkedHashMap<>();
        for (ItemVendaRequest item : request.itens()) {
            if (item == null || item.codigo() == null || item.quantidade() == null || item.quantidade() <= 0) {
                throw new DadosInvalidosException("Item inválido: código obrigatório e quantidade deve ser maior que zero.");
            }
            quantidadesPorCodigo.merge(item.codigo(), item.quantidade(), Integer::sum);
        }

        // 2. Valida existência e saldo de TODOS os produtos antes de debitar (Atomicidade)
        Map<Integer, Produto> produtosValidados = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : quantidadesPorCodigo.entrySet()) {
            int codigo = entry.getKey();
            int qtdSolicitada = entry.getValue();

            Produto produto = produtos.get(codigo);
            if (produto == null) {
                throw new ProdutoNaoEncontradoException("Produto não encontrado com o código " + codigo + ".");
            }
            if (produto.getEstoque() < qtdSolicitada) {
                throw new DadosInvalidosException("Estoque insuficiente para o produto " + codigo);
            }
            produtosValidados.put(codigo, produto);
        }

        // 3. Debita estoques, calcula subtotais e soma o total
        List<ItemVendaResponse> itensResposta = new ArrayList<>();
        BigDecimal totalVenda = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> entry : quantidadesPorCodigo.entrySet()) {
            int codigo = entry.getKey();
            int qtd = entry.getValue();
            Produto produto = produtosValidados.get(codigo);

            produto.setEstoque(produto.getEstoque() - qtd);

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(qtd))
                    .setScale(2, RoundingMode.HALF_UP);

            totalVenda = totalVenda.add(subtotal);

            itensResposta.add(new ItemVendaResponse(
                    codigo,
                    qtd,
                    subtotal,
                    produto.getEstoque()
            ));
        }

        return new VendaResponse(totalVenda.setScale(2, RoundingMode.HALF_UP), itensResposta);
    }

}

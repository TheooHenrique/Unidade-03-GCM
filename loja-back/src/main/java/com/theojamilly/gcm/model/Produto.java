package com.theojamilly.gcm.model;

public class Produto {
    private Integer codigo;
    private Double preco;
    private Integer estoque;

    public Produto(){ }

    public Produto( Integer codigo, Double preco, Integer estoque) {
        this.codigo = codigo;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

}
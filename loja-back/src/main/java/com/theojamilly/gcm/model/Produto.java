package com.theojamilly.gcm.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor 
@Data
public class Produto {
    private Integer codigo;
    private BigDecimal preco;
    private Integer estoque;
}
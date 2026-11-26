package com.theojamilly.gcm.dto;

import java.math.BigDecimal;

public record ItemVendaResponse(int codigo, int quantidade, BigDecimal subtotal, int estoque) {
    
}

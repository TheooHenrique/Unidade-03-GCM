package com.theojamilly.gcm.dto;

import java.math.BigDecimal;
import java.util.List;

public record VendaResponse(BigDecimal total, List<ItemVendaResponse> itens) {
    
}

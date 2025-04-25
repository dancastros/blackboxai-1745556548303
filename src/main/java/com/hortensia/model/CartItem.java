package com.hortensia.model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItem {
    private Product product;
    private Integer quantity;
    private BigDecimal unitPrice;
    
    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }
    
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

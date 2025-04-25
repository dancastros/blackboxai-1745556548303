package com.hortensia.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private Map<Long, CartItem> items = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;

    public void addItem(Product product, int quantity) {
        CartItem item = items.get(product.getId());
        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            items.put(product.getId(), new CartItem(product, quantity));
        }
        recalculateTotal();
    }

    public void updateItemQuantity(Long productId, int quantity) {
        CartItem item = items.get(productId);
        if (item != null) {
            if (quantity > 0) {
                item.setQuantity(quantity);
                recalculateTotal();
            } else {
                removeItem(productId);
            }
        }
    }

    public void removeItem(Long productId) {
        items.remove(productId);
        recalculateTotal();
    }

    public void clear() {
        items.clear();
        total = BigDecimal.ZERO;
    }

    public List<CartItem> getItemsList() {
        return new ArrayList<>(items.values());
    }

    private void recalculateTotal() {
        total = items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public CartItem getItem(Long productId) {
        return items.get(productId);
    }
}

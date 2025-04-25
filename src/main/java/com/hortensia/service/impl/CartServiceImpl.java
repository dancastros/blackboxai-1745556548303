package com.hortensia.service.impl;

import com.hortensia.model.*;
import com.hortensia.service.CartService;
import com.hortensia.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final Cart cart;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(Cart cart, ProductService productService) {
        this.cart = cart;
        this.productService = productService;
    }

    @Override
    public void addToCart(Long productId, int quantity) {
        productService.getProductById(productId).ifPresent(product -> {
            if (productService.isStockAvailable(productId, quantity)) {
                cart.addItem(product, quantity);
            } else {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
        });
    }

    @Override
    public void updateCartItemQuantity(Long productId, int quantity) {
        CartItem item = cart.getItem(productId);
        if (item != null) {
            if (quantity <= 0) {
                removeFromCart(productId);
            } else if (productService.isStockAvailable(productId, quantity)) {
                cart.updateItemQuantity(productId, quantity);
            } else {
                throw new RuntimeException("Insufficient stock for product: " + item.getProduct().getName());
            }
        }
    }

    @Override
    public void removeFromCart(Long productId) {
        cart.removeItem(productId);
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public List<CartItem> getCartItems() {
        return cart.getItemsList();
    }

    @Override
    public boolean isProductInCart(Long productId) {
        return cart.getItem(productId) != null;
    }

    @Override
    public Order createOrderFromCart(User user) {
        if (cart.isEmpty()) {
            throw new RuntimeException("Cannot create order from empty cart");
        }

        if (!validateCart()) {
            throw new RuntimeException("One or more items in cart have insufficient stock");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(cart.getTotal());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItemsList()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(cartItem.getUnitPrice());
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        return order;
    }

    @Override
    public boolean validateCart() {
        for (CartItem item : cart.getItemsList()) {
            if (!productService.isStockAvailable(item.getProduct().getId(), item.getQuantity())) {
                return false;
            }
        }
        return true;
    }
}

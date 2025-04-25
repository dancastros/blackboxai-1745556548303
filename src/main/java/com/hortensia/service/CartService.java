package com.hortensia.service;

import com.hortensia.model.Cart;
import com.hortensia.model.CartItem;
import com.hortensia.model.Order;
import com.hortensia.model.User;

import java.util.List;

public interface CartService {
    void addToCart(Long productId, int quantity);
    void updateCartItemQuantity(Long productId, int quantity);
    void removeFromCart(Long productId);
    void clearCart();
    Cart getCart();
    List<CartItem> getCartItems();
    boolean isProductInCart(Long productId);
    Order createOrderFromCart(User user);
    boolean validateCart();
}

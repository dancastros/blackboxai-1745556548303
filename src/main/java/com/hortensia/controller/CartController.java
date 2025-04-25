package com.hortensia.controller;

import com.hortensia.model.Cart;
import com.hortensia.model.CartItem;
import com.hortensia.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("cartItems", cart.getItemsList());
        model.addAttribute("total", cart.getTotal());
        return "cart";
    }

    @PostMapping("/add/{productId}")
    @ResponseBody
    public ResponseEntity<?> addToCart(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            cartService.addToCart(productId, quantity);
            Cart cart = cartService.getCart();
            return ResponseEntity.ok(Map.of(
                "message", "Product added to cart",
                "cartSize", cart.getItemCount(),
                "cartTotal", cart.getTotal()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/update/{productId}")
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@PathVariable Long productId, @RequestParam int quantity) {
        try {
            cartService.updateCartItemQuantity(productId, quantity);
            Cart cart = cartService.getCart();
            return ResponseEntity.ok(Map.of(
                "message", "Cart updated",
                "cartSize", cart.getItemCount(),
                "cartTotal", cart.getTotal()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        Cart cart = cartService.getCart();
        return ResponseEntity.ok(Map.of(
            "message", "Item removed from cart",
            "cartSize", cart.getItemCount(),
            "cartTotal", cart.getTotal()
        ));
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }

    @GetMapping("/items")
    @ResponseBody
    public List<CartItem> getCartItems() {
        return cartService.getCartItems();
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartService.getCart();
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cartItems", cart.getItemsList());
        model.addAttribute("total", cart.getTotal());
        return "checkout";
    }
}

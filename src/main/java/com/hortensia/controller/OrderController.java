package com.hortensia.controller;

import com.hortensia.model.Order;
import com.hortensia.model.User;
import com.hortensia.service.CartService;
import com.hortensia.service.OrderService;
import com.hortensia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {
        if (cartService.getCart().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("user", new User());
        model.addAttribute("cart", cartService.getCart());
        return "checkout";
    }

    @PostMapping("/process")
    public String processOrder(@Valid @ModelAttribute("user") User user, 
                             BindingResult result, 
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cart", cartService.getCart());
            return "checkout";
        }

        try {
            // Save or update user
            user = userService.saveUser(user);

            // Create order from cart
            Order order = cartService.createOrderFromCart(user);

            // Process the order
            orderService.processOrder(order);

            // Clear the cart after successful order
            cartService.clearCart();

            return "redirect:/orders/success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cart", cartService.getCart());
            return "checkout";
        }
    }

    @GetMapping("/success")
    public String orderSuccess() {
        return "order-success";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        orderService.getOrderById(id).ifPresent(order -> 
            model.addAttribute("order", order)
        );
        return "order-details";
    }

    @PostMapping("/validate-cart")
    @ResponseBody
    public ResponseEntity<?> validateCart() {
        boolean isValid = cartService.validateCart();
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "valid", false,
                "message", "One or more items in your cart are no longer available in the requested quantity"
            ));
        }
    }
}

package com.hortensia.controller;

import com.hortensia.model.Product;
import com.hortensia.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAvailableProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(product -> 
            model.addAttribute("product", product)
        );
        return "product-details";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> searchResults = productService.searchProducts(query);
        model.addAttribute("products", searchResults);
        model.addAttribute("searchQuery", query);
        return "products";
    }

    @PostMapping("/{id}/stock")
    @ResponseBody
    public boolean checkStock(@PathVariable Long id, @RequestParam int quantity) {
        return productService.isStockAvailable(id, quantity);
    }
}

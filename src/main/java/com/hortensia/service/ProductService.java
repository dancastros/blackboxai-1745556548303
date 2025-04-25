package com.hortensia.service;

import com.hortensia.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    List<Product> searchProducts(String query);
    List<Product> getAvailableProducts();
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    boolean updateStock(Long productId, int quantity);
    boolean isStockAvailable(Long productId, int quantity);
}

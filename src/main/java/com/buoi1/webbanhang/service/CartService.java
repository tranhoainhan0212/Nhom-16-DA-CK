package com.buoi1.webbanhang.service;

import com.buoi1.webbanhang.model.CartItem;
import com.buoi1.webbanhang.model.Product;
import com.buoi1.webbanhang.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@SessionScope
public class CartService {
    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductRepository productRepository;

    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        // Check if the product already exists in the cart
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                // If the product exists, update the quantity
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // If the product does not exist, add it to the cart
        cartItems.add(new CartItem(product, quantity));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void removeFromCart(Long productId) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProduct().getId().equals(productId)) {
                iterator.remove();
                return;
            }
        }
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }

    public void updateCartItem(Long productId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }
    public double calculateTotalPrice() {
        return cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}


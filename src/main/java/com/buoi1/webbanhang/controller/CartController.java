package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.model.CartItem;
import com.buoi1.webbanhang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalAmount = cartService.getTotal(); // Lấy tổng tiền từ service
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart/cart"; // Thay đổi view name từ "/cart/cart" thành "cart/cart"
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateCartItem(productId, quantity);
        return ResponseEntity.ok("Cart item updated successfully.");
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}
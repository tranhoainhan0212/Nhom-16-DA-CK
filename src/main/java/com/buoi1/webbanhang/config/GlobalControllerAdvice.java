package com.buoi1.webbanhang.config;

import com.buoi1.webbanhang.model.CartItem;
import com.buoi1.webbanhang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final CartService cartService;

    @Autowired
    public GlobalControllerAdvice(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartItems")
    public List<CartItem> cartItems() {
        return cartService.getCartItems();
    }

    @ModelAttribute("total")
    public double total() {
        return cartService.calculateTotalPrice();
    }
}

package com.buoi1.webbanhang.service;

import com.buoi1.webbanhang.model.CartItem;
import com.buoi1.webbanhang.model.Order;
import com.buoi1.webbanhang.model.OrderDetail;
import com.buoi1.webbanhang.repository.OrderDetailRepository;
import com.buoi1.webbanhang.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;

    public Order createOrder(String customerName, List<CartItem> cartItems) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }

        // Clear the cart after order placement
        cartService.clearCart();

        return order;
    }
}

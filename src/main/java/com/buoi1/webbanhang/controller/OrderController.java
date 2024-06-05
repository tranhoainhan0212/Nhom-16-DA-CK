package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.config.Config;
import com.buoi1.webbanhang.model.CartItem;
import com.buoi1.webbanhang.service.CartService;
import com.buoi1.webbanhang.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        double totalAmount = cartService.getTotal();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart/checkout";
    }

    @PostMapping("/submit")
    public String submitOrder(String customerName) {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName, cartItems);
        return "redirect:/order/confirmation";
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
    @GetMapping("/payment/vnpay")
    public String paymentVNPay(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_TmnCode = Config.vnp_TmnCode;
        String vnp_HashSecret = Config.secretKey;

        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_OrderInfo = "Thanh toan don hang " + vnp_TxnRef;
        String vnp_OrderType = "other";
        String vnp_Locale = "vn";
        String vnp_ReturnUrl = Config.vnp_ReturnUrl;
        String vnp_Version = Config.vnp_Version;
        String vnp_Command = Config.vnp_Command;

        long amount = (long) (cartService.calculateTotalPrice() * 100);
        String vnp_Amount = String.valueOf(amount);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                // Build query data
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        return "redirect:" + paymentUrl;
    }

    @GetMapping("/vnpay_cancel")
    public String vnpayCancel(HttpServletRequest request, Model model) {
        // Xử lý hủy thanh toán ở đây
        return "cart/cancel"; // Đường dẫn tới trang thông báo hủy thanh toán
    }

    @GetMapping("/vnpay_return")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");

        if ("00".equals(vnp_ResponseCode)) {
            return "cart/success";
        } else {
            return "cart/failure";
        }
    }
}

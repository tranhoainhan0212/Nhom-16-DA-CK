package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.config.Config;
import com.buoi1.webbanhang.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/payment")
public class PaymentVnpayController {

    @Autowired
    private CartService cartService;

    @GetMapping("/vnpay")
    public void payWithVNPay(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_OrderInfo = "Thanh toan don hang";
        String vnp_OrderType = "billpayment";
        String vnp_Amount = String.valueOf((long) (cartService.calculateTotalPrice() * 100));
        String vnp_Locale = "vn";
        String vnp_BankCode = "";
        String vnp_IpAddr = Config.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        // Build data to hash and query string
        String hashData = Config.hashAllFields(vnp_Params);
        vnp_Params.put("vnp_SecureHash", hashData);

        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (query.length() > 0) {
                query.append('&');
            }
            query.append(entry.getKey()).append('=').append(entry.getValue());
        }

        String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();
        response.sendRedirect(paymentUrl);
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
            return "cart/success"; // Đường dẫn tới trang thành công
        } else {
            return "cart/failure"; // Đường dẫn tới trang thất bại
        }
    }
}

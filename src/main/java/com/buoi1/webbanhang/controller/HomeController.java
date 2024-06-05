package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.model.Product;
import com.buoi1.webbanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    /**
     * Chuyển đến trang danh sách sản phẩm khi truy cập vào trang chủ.
     *
     * @param model   Đối tượng Model để chuyển dữ liệu sang view.
     * @param keyword Từ khóa tìm kiếm (nếu có).
     * @param page    Số trang hiện tại (mặc định là 0).
     * @param size    Kích thước trang (mặc định là 10).
     * @return Tên view (trang) để hiển thị.
     */
    @GetMapping("/")
    public String home(Model model,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty()) {
            productPage = productService.searchProductsByName(keyword, pageable);
        } else {
            productPage = productService.getAllProducts(pageable);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);

        return "index";  // Đảm bảo trả về đúng tên view (trang chủ)
    }

    @GetMapping("/home")
    public String hello(Model model) {
        model.addAttribute("message", "XIN CHÀO TRƯỜNG ĐẠI HỌC CÔNG NGHỆ THÀNH PHỐ HỒ CHÍ MINH!");
        return "home";
    }

    /**
     * Kiểm tra xem người dùng đã đăng nhập và có quyền ADMIN hay USER không.
     *
     * @return true nếu người dùng đã đăng nhập và có quyền ADMIN hoặc USER, ngược lại false.
     */
    public boolean isLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !authentication.getAuthorities().isEmpty();
    }
}

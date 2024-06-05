package com.buoi1.webbanhang.controller;

import com.buoi1.webbanhang.model.Product;
import com.buoi1.webbanhang.service.CategoryService;
import com.buoi1.webbanhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String showProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        int size = 8;
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

        return "/products/product-list";
    }



    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                Path path = Paths.get(uploadPath + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "/products/add-product";
            }
        }

        productService.addProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/products/update-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/products/update-product";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = imageFile.getOriginalFilename();
                Path path = Paths.get(uploadPath + fileName);
                Files.write(path, imageFile.getBytes());
                product.setImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "/products/update-product";
            }
        }

        productService.updateProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "/products/product-detail";
    }

    @GetMapping("/management")
    public String showProductManagement() {
        return "/products/product-management";
    }
}

package com.buoi1.webbanhang.service;

import com.buoi1.webbanhang.model.Product;
import com.buoi1.webbanhang.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Lấy tất cả sản phẩm từ cơ sở dữ liệu.
     *
     * @return Danh sách tất cả sản phẩm.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Lấy sản phẩm theo ID.
     *
     * @param id ID của sản phẩm.
     * @return Một đối tượng Optional chứa sản phẩm nếu tìm thấy.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Thêm một sản phẩm mới vào cơ sở dữ liệu.
     *
     * @param product Sản phẩm cần thêm.
     * @return Sản phẩm đã được thêm.
     */
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Cập nhật thông tin sản phẩm.
     *
     * @param product Sản phẩm với thông tin cần cập nhật.
     * @return Sản phẩm đã được cập nhật.
     */
    public Product updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Sản phẩm với ID " +
                        product.getId() + " không tồn tại."));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());

        if (product.getImage() != null) {
            existingProduct.setImage(product.getImage());
        }

        return productRepository.save(existingProduct);
    }

    /**
     * Xóa sản phẩm theo ID.
     *
     * @param id ID của sản phẩm cần xóa.
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }

    /**
     * Tìm kiếm sản phẩm theo tên.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @return Danh sách sản phẩm chứa từ khóa.
     */


    public Page<Product> searchProductsByName(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }


    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }


}

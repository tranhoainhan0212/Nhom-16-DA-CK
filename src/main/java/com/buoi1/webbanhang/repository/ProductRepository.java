package com.buoi1.webbanhang.repository;

import com.buoi1.webbanhang.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
}

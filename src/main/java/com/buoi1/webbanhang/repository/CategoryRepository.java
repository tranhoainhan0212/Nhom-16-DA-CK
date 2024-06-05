package com.buoi1.webbanhang.repository;


import com.buoi1.webbanhang.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
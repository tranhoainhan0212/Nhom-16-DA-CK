package com.buoi1.webbanhang.model;

import com.buoi1.webbanhang.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(nullable = false)
    private int quantity;



}

package com.buoi1.webbanhang.model;

public class CartItem {
    private Product product;
    private int quantity;

    // Constructors
    public CartItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Override equals and hashCode for proper comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartItem cartItem = (CartItem) o;

        if (!product.equals(cartItem.product)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return product.hashCode();
    }
}

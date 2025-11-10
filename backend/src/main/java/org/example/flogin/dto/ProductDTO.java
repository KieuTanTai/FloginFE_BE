package org.example.flogin.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String name;
    private String imageUrl;
    private BigDecimal price;
    private String description;
    private CategoryDTO category;
    private Integer quantity;
    private Boolean deleted;

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String imageUrl, BigDecimal price,
            String description, CategoryDTO category, Integer quantity, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.deleted = deleted;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}

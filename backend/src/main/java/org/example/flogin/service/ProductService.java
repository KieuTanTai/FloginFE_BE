package org.example.flogin.service;

import org.example.flogin.dto.CategoryDTO;
import org.example.flogin.dto.ProductDTO;
import org.example.flogin.entity.Category;
import org.example.flogin.entity.Product;
import org.example.flogin.repository.CategoryRepository;
import org.example.flogin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Business validation
        validateProductBusinessRules(productDTO);

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity() != null ? productDTO.getQuantity() : 10);
        product.setDeleted(false);

        // Set category if provided - validate category exists in DB
        if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
            Category category = categoryRepository.findById(Objects.requireNonNull(productDTO.getCategory().getId()))
                    .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại trong hệ thống"));
            product.setCategory(category);
        } else {
            throw new RuntimeException("Thể loại không được để trống");
        }

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Business validation rules
    private void validateProductBusinessRules(ProductDTO productDTO) {
        // Name validation
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        if (productDTO.getName().length() < 3 || productDTO.getName().length() > 100) {
            throw new IllegalArgumentException("Tên sản phẩm phải có từ 3 đến 100 ký tự");
        }

        // Price validation
        if (productDTO.getPrice() == null || productDTO.getPrice().doubleValue() < 0
                || productDTO.getPrice().doubleValue() > 999999999) {
            throw new IllegalArgumentException("Giá phải từ 0 đến 999,999,999");
        }

        // Quantity validation
        if (productDTO.getQuantity() != null) {
            if (productDTO.getQuantity() < 0 || productDTO.getQuantity() > 99999) {
                throw new IllegalArgumentException("Số lượng phải từ 0 đến 99,999");
            }
        }

        // Description validation
        if (productDTO.getDescription() != null && productDTO.getDescription().length() > 500) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 500 ký tự");
        }

        // Category validation
        if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Thể loại không được để trống");
        }

        // Validate category exists in database
        if (!categoryRepository.existsById(Objects.requireNonNull(productDTO.getCategory().getId()))) {
            throw new IllegalArgumentException("Thể loại không tồn tại trong hệ thống");
        }
    }

    // Get all products (including deleted for admin view)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get product by ID (not deleted)
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .map(this::convertToDTO);
    }

    // Search products by name (not deleted)
    public List<ProductDTO> searchProductsByName(String name) {
        return productRepository.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // Business validation
        validateProductBusinessRules(productDTO);

        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found or has been deleted"));

        if (productDTO.getName() != null)
            product.setName(productDTO.getName());
        if (productDTO.getImageUrl() != null)
            product.setImageUrl(productDTO.getImageUrl());
        if (productDTO.getPrice() != null)
            product.setPrice(productDTO.getPrice());
        if (productDTO.getDescription() != null)
            product.setDescription(productDTO.getDescription());
        if (productDTO.getQuantity() != null)
            product.setQuantity(productDTO.getQuantity());

        // Update category if provided - validate category exists in DB
        if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
            Category category = categoryRepository.findById(Objects.requireNonNull(productDTO.getCategory().getId()))
                    .orElseThrow(() -> new RuntimeException("Thể loại không tồn tại trong hệ thống"));
            product.setCategory(category);
        }

        if (product == null)
            throw new IllegalArgumentException("Product must not be null");

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    // Soft delete product
    public void softDeleteProduct(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Product id must not be null");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setDeleted(true);
        productRepository.save(product);
    }

    // Restore soft deleted product
    public ProductDTO restoreProduct(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Product id must not be null");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setDeleted(false);
        Product restoredProduct = productRepository.save(product);
        return convertToDTO(restoredProduct);
    }

    // Hard delete product (permanent)
    public void hardDeleteProduct(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Product id must not be null");
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    // Update product quantity
    public ProductDTO updateQuantity(Long id, Integer quantity) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Product not found or has been deleted"));

        product.setQuantity(quantity);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    // Convert Entity to DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity());
        dto.setDeleted(product.getDeleted());

        // Convert category to DTO
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            categoryDTO.setDescription(product.getCategory().getDescription());
            dto.setCategory(categoryDTO);
        }

        return dto;
    }
}

package org.example.flogin.service;

import org.example.flogin.dto.ProductDTO;
import org.example.flogin.entity.Product;
import org.example.flogin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setAuthor(productDTO.getAuthor());
        product.setPublicationYear(productDTO.getPublicationYear());
        product.setQuantity(productDTO.getQuantity() != null ? productDTO.getQuantity() : 10);
        product.setDeleted(false);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    // Get all products (not deleted)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get product by ID (not deleted)
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .map(this::convertToDTO);
    }

    // Get products by author (not deleted)
    public List<ProductDTO> getProductsByAuthor(String author) {
        return productRepository.findByAuthorAndDeletedFalse(author).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get products by publication year (not deleted)
    public List<ProductDTO> getProductsByYear(Integer year) {
        return productRepository.findByPublicationYearAndDeletedFalse(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Search products by name (not deleted)
    public List<ProductDTO> searchProductsByName(String name) {
        return productRepository.searchByName(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update product
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
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
        if (productDTO.getAuthor() != null)
            product.setAuthor(productDTO.getAuthor());
        if (productDTO.getPublicationYear() != null)
            product.setPublicationYear(productDTO.getPublicationYear());
        if (productDTO.getQuantity() != null)
            product.setQuantity(productDTO.getQuantity());
        
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
        dto.setAuthor(product.getAuthor());
        dto.setPublicationYear(product.getPublicationYear());
        dto.setQuantity(product.getQuantity());
        return dto;
    }
}

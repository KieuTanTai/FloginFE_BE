package org.example.flogin.controller;

import org.example.flogin.dto.ProductDTO;
import org.example.flogin.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Validate product DTO input
    private void validateProductInput(ProductDTO productDTO) {
        // Validate name (3-100 words, not empty)
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        String[] words = productDTO.getName().trim().split("\\s+");
        if (words.length < 3) {
            throw new IllegalArgumentException("Tên sản phẩm phải có ít nhất 3 từ");
        }
        if (words.length > 100) {
            throw new IllegalArgumentException("Tên sản phẩm không được vượt quá 100 từ");
        }

        // Validate price (0 - 999,999,999)
        if (productDTO.getPrice() == null) {
            throw new IllegalArgumentException("Giá không được để trống");
        }
        if (productDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá phải lớn hơn hoặc bằng 0");
        }
        if (productDTO.getPrice().compareTo(new BigDecimal("999999999")) > 0) {
            throw new IllegalArgumentException("Giá không được vượt quá 999,999,999");
        }

        // Validate quantity (0 - 99,999)
        if (productDTO.getQuantity() != null) {
            if (productDTO.getQuantity() < 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn hoặc bằng 0");
            }
            if (productDTO.getQuantity() > 99999) {
                throw new IllegalArgumentException("Số lượng không được vượt quá 99,999");
            }
        }

        // Validate description (max 500 characters)
        if (productDTO.getDescription() != null && productDTO.getDescription().length() > 500) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 500 ký tự");
        }

        // Validate category (must be present)
        if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Thể loại không được để trống");
        }
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            validateProductInput(productDTO);
            ProductDTO created = productService.createProduct(productDTO);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        try {
            validateProductInput(productDTO);
            ProductDTO updated = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteProduct(@PathVariable Long id) {
        try {
            productService.softDeleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<ProductDTO> restoreProduct(@PathVariable Long id) {
        try {
            ProductDTO restored = productService.restoreProduct(id);
            return ResponseEntity.ok(restored);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductDTO> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        ProductDTO updated = productService.updateQuantity(id, quantity);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}

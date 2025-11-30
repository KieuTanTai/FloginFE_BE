package org.example.flogin;

import org.example.flogin.dto.CategoryDTO;
import org.example.flogin.dto.ProductDTO;
import org.example.flogin.entity.Category;
import org.example.flogin.entity.Product;
import org.example.flogin.repository.CategoryRepository;
import org.example.flogin.repository.ProductRepository;
import org.example.flogin.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ProductService Unit Test")
class ProductServiceUnitTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    private Category category;
    private CategoryDTO categoryDTO;
    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setId(1L);
        category.setName("Laptop");
        category.setDescription("Laptop category");
        categoryDTO = new CategoryDTO(1L, "Laptop", "Laptop category");
        product = new Product("Macbook Air", "test_img.jpg", new BigDecimal("1000"), "test item", category, 5);
        product.setId(1L);
        productDTO = new ProductDTO(1L, "Macbook Air", "test_img.jpg", new BigDecimal("1000"), "test item", categoryDTO,
                5, false);
    }

    @Test
    @DisplayName("createProduct - success")
    void testCreateProductSuccess() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productDTO = new ProductDTO(null, "Macbook Air", "test_img.jpg", new BigDecimal("1000"), "desc", categoryDTO, 5,
                false);
        ProductDTO result = productService.createProduct(productDTO);
        assertEquals("Macbook Air", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("createProduct - validation error: name empty")
    void testCreateProductValidationErrorNameEmpty() {
        productDTO = new ProductDTO(null, "", "img.jpg", new BigDecimal("1000"), "desc", categoryDTO, 5, false);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> productService.createProduct(productDTO));
        assertTrue(ex.getMessage().contains("Tên sản phẩm không được để trống"));
    }

    @Test
    @DisplayName("getProduct - success")
    void testGetProductSuccess() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        Optional<ProductDTO> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        assertEquals("Macbook Air", result.get().getName());
    }

    @Test
    @DisplayName("getProduct - not found")
    void testGetProductNotFound() {
        when(productRepository.findByIdAndDeletedFalse(2L)).thenReturn(Optional.empty());
        Optional<ProductDTO> result = productService.getProductById(2L);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("updateProduct - success")
    void testUpdateProductSuccess() {
        ProductDTO updateDTO = new ProductDTO(1L, "Macbook Pro", "img2.jpg", new BigDecimal("1200"), "desc2",
                categoryDTO, 10, false);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productDTO = productService.updateProduct(1L, updateDTO);
        assertEquals("Macbook Pro", productDTO.getName());
        assertEquals(new BigDecimal("1200"), productDTO.getPrice());
    }

    @Test
    @DisplayName("updateProduct - not found")
    void testUpdateProductNotFound() {
        ProductDTO updateDTO = new ProductDTO(1L, "Macbook Pro 2022", "img2.jpg", new BigDecimal("1200"), "desc2",
                categoryDTO, 10, false);
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> productService.updateProduct(1L, updateDTO));
        assertTrue(ex.getMessage().contains("Product not found or has been deleted"));
    }

    @Test
    @DisplayName("deleteProduct - success")
    void testDeleteProductSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        assertDoesNotThrow(() -> productService.softDeleteProduct(1L));
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("deleteProduct - not found")
    void testDeleteProductNotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> productService.softDeleteProduct(2L));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("getAllProducts - success")
    void testGetAllProductsSuccess() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<ProductDTO> result = productService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("Macbook Air", result.get(0).getName());
    }

    @Test
    @DisplayName("pagination - getAllProducts with page size")
    void testGetAllProductsPagination() {
        Product product2 = new Product("Macbook Pro 2023", "img2.jpg", new BigDecimal("1200"), "desc2", category, 10);
        product2.setId(2L);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));
        List<ProductDTO> result = productService.getAllProducts();
        assertEquals(2, result.size());
        // Simulate pagination: get first page size 1
        List<ProductDTO> page1 = result.subList(0, 1);
        assertEquals(1, page1.size());
        assertEquals("Macbook Air", page1.get(0).getName());
    }
}

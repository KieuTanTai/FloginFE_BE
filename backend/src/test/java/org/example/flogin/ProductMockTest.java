package org.example.flogin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.example.flogin.dto.CategoryDTO;
import org.example.flogin.dto.ProductDTO;
import org.example.flogin.entity.Category;
import org.example.flogin.entity.Product;
import org.example.flogin.repository.CategoryRepository;
import org.example.flogin.repository.ProductRepository;
import org.example.flogin.service.ProductService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService service;

    private ProductDTO dto;
    private Category category;
    private Product product;

    @BeforeEach
    void setup() {
        category = new Category();
        category.setId(1L);
        category.setName("Laptop");

        dto = new ProductDTO();
        dto.setName("MacBook Pro");
        dto.setPrice(new BigDecimal("2000"));
        dto.setQuantity(5);
        dto.setDescription("Good laptop");
        dto.setImageUrl("img.png");

        CategoryDTO catDTO = new CategoryDTO();
        catDTO.setId(1L);
        dto.setCategory(catDTO);

        product = new Product();
        product.setId(1L);
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setCategory(category);
        product.setDeleted(false);
    }

    // ---------------------------------------------------------
    // 1) CREATE PRODUCT
    // ---------------------------------------------------------

    @Test
    void createProduct_success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = service.createProduct(dto);

        assertEquals("MacBook Pro", result.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_categoryNotExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(dto));
    }

    @Test
    void createProduct_missingCategory() {
        dto.setCategory(null);
        assertThrows(IllegalArgumentException.class, () -> service.createProduct(dto));
    }

    // ---------------------------------------------------------
    // 2) GET ALL PRODUCTS
    // ---------------------------------------------------------

    @Test
    void getAllProducts_success() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = service.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("MacBook Pro", result.get(0).getName());
        verify(productRepository).findAll();

    }

    // ---------------------------------------------------------
    // 3) GET PRODUCT BY ID
    // ---------------------------------------------------------

    @Test
    void getProductById_success() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(product));

        Optional<ProductDTO> result = service.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("MacBook Pro", result.get().getName());
        verify(productRepository).findByIdAndDeletedFalse(1L);
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        Optional<ProductDTO> result = service.getProductById(1L);

        assertTrue(result.isEmpty());
    }

    // ---------------------------------------------------------
    // 4) SEARCH PRODUCTS
    // ---------------------------------------------------------

    @Test
    void searchProductsByName_success() {
        when(productRepository.searchByName("mac"))
                .thenReturn(List.of(product));

        List<ProductDTO> result = service.searchProductsByName("mac");

        assertEquals(1, result.size());
        verify(productRepository).searchByName("mac");

    }

    // ---------------------------------------------------------
    // 5) UPDATE PRODUCT
    // ---------------------------------------------------------

    @Test
    void updateProduct_success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductDTO result = service.updateProduct(1L, dto);

        assertEquals("MacBook Pro", result.getName());
        verify(productRepository).save(product);
    }

    @Test
    void updateProduct_notFound() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateProduct(1L, dto));
    }

    // ---------------------------------------------------------
    // 6) SOFT DELETE
    // ---------------------------------------------------------

    @Test
    void softDeleteProduct_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        service.softDeleteProduct(1L);

        assertTrue(product.getDeleted());
        verify(productRepository).save(product);
    }

    @Test
    void softDeleteProduct_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.softDeleteProduct(1L));
    }

    // ---------------------------------------------------------
    // 7) RESTORE PRODUCT
    // ---------------------------------------------------------

    @Test
    void restoreProduct_success() {
        product.setDeleted(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductDTO result = service.restoreProduct(1L);

        assertFalse(result.getDeleted());
        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
    }

    @Test
    void restoreProduct_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.restoreProduct(1L));
        
    }

    // ---------------------------------------------------------
    // 8) HARD DELETE
    // ---------------------------------------------------------

    @Test
    void hardDeleteProduct_success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        service.hardDeleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void hardDeleteProduct_notExists() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.hardDeleteProduct(1L));
    }

    // ---------------------------------------------------------
    // 9) UPDATE QUANTITY
    // ---------------------------------------------------------

    @Test
    void updateQuantity_success() {
        when(productRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        ProductDTO result = service.updateQuantity(1L, 99);

        assertEquals(99, result.getQuantity());
        verify(productRepository).findByIdAndDeletedFalse(1L);
        verify(productRepository).save(product);
    }

    @Test
    void updateQuantity_notFound() {
        when(productRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.updateQuantity(1L, 10));
    }
}

package org.example.flogin;

import org.example.flogin.controller.ProductController;
import org.example.flogin.service.ProductService;
import org.example.flogin.dto.CategoryDTO;
import org.example.flogin.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test POST /api/products (Create)")
    void createProductTest() throws Exception {

        ProductDTO input = new ProductDTO(
                "Dell G15",
                "https://example.com/dell.jpg",
                new BigDecimal("19990000"),
                "Laptop gaming",
                new CategoryDTO(1L, "Gaming", "desc"),
                10,
                false
        );

        when(productService.createProduct(any(ProductDTO.class))).thenReturn(input);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dell G15"));
    }

    @Test
    @DisplayName("Test GET /api/products (Read all)")
    void getProductTest() throws Exception {
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO(
                        "Dell G15",
                        "https://example.com/dell.jpg",
                        new BigDecimal("19990000"),
                        "Laptop gaming",
                        new CategoryDTO(1L, "Gaming", "desc"),
                        10,
                        false
                ),
                new ProductDTO(
                        "ASUS GAMING F15",
                        "https://example.com/dell.jpg",
                        new BigDecimal("19990000"),
                        "Laptop gaming",
                        new CategoryDTO(1L, "Gaming", "desc"),
                        10,
                        false
                )
        );

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Dell G15"));
    }

    @Test
    @DisplayName("Test GET /api/products/{id} (Read one)")
    void getProductByIdTest() throws Exception {
        ProductDTO product=new ProductDTO(
                1L,
                "Dell G15",
                "https://example.com/dell.jpg",
                new BigDecimal("19990000"),
                "Laptop gaming",
                new CategoryDTO(1L, "Gaming", "desc"),
                10,
                false
        );
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @DisplayName("Test PUT /api/products/{id} (Update)")
        void updateProductTest() throws Exception {

        ProductDTO product = new ProductDTO(
                1L,
                "Dell G15",
                "https://example.com/dell.jpg",
                new BigDecimal("19990000"),
                "Laptop gaming",
                new CategoryDTO(1L, "Gaming", "desc"),
                10,
                false
        );

        when(productService.updateProduct(eq(1L), any(ProductDTO.class)))
                .thenReturn(product);


        mockMvc.perform(put("/api/products/{id}", 1L)    // ← PATH VARIABLE
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))  // ← BODY JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dell G15"));
    }

    @Test
    @DisplayName("Test DELETE /api/products/{id} (Delete)")
    void deleteProductTest() throws Exception{

        doNothing().when(productService).softDeleteProduct(1L);

        mockMvc.perform(delete("/api/products/{id}",1L))
                .andExpect(status().isNoContent());
    }
}

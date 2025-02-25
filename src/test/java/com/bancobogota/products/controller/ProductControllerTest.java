package com.bancobogota.products.controller;

import com.bancobogota.products.model.Product;
import com.bancobogota.products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetProducts() throws Exception {
        Product product1 = new Product("1", "Laptop", "Electronics", 1200.0, 10);
        Product product2 = new Product("2", "Phone", "Electronics", 800.0, 20);

        when(productService.getAll()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Phone"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        Product product = new Product("1", "Laptop", "Electronics", 1200.0, 10);
        when(productService.getById("1")).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getById("1")).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product("1", "Laptop", "Electronics", 1200.0, 10);
        when(productService.save(any(Product.class))).thenReturn(product);

        String jsonRequest = """
            {
                "id": "1",
                "name": "Laptop",
                "category": "Electronics",
                "price": 1200.0,
                "stock": 10
            }
        """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product("1", "Laptop Pro", "Electronics", 1500.0, 5);
        when(productService.update(eq("1"), any(Product.class))).thenReturn(updatedProduct);

        String jsonRequest = """
            {
                "id": "1",
                "name": "Laptop Pro",
                "category": "Electronics",
                "price": 1500.0,
                "stock": 5
            }
        """;

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"))
                .andExpect(jsonPath("$.price").value(1500.0))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).delete("1");

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}

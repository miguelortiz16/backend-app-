package com.bancobogota.products.service.impl;



import com.bancobogota.products.constants.ErrorMessages;
import com.bancobogota.products.exception.CustomException;
import com.bancobogota.products.model.Product;
import com.bancobogota.products.repository.ProductRepository;
import com.bancobogota.products.service.impl.ProductServiceImpl;
import com.bancobogota.products.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId("1");
        product.setName("Laptop");
        product.setCategory("Electronics");
        product.setPrice(1500.0);
        product.setStock(10);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getById("1");

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getName());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.getById("1"));

        assertEquals(ErrorMessages.PRODUCT_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testSaveProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.save(product);

        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getName());
        verify(productValidator, times(1)).validate(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Laptop");
        updatedProduct.setCategory("Electronics");
        updatedProduct.setPrice(1700.0);
        updatedProduct.setStock(8);

        Product result = productService.update("1", updatedProduct);

        assertEquals("Updated Laptop", result.getName());
        assertEquals(1700.0, result.getPrice());
        verify(productValidator, times(1)).validate(updatedProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> productService.update("1", product));

        assertEquals(ErrorMessages.PRODUCT_NOT_FOUND, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById("1")).thenReturn(true);
        doNothing().when(productRepository).deleteById("1");

        assertDoesNotThrow(() -> productService.delete("1"));
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById("1")).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> productService.delete("1"));

        assertEquals(ErrorMessages.PRODUCT_DELETE_ERROR, exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}



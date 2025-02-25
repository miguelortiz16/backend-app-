package com.bancobogota.products.exception;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleCustomException() {
        // Given
        CustomException customException = new CustomException("Error personalizado", HttpStatus.BAD_REQUEST);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleCustomException(customException);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Error personalizado", response.getBody().get("message"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().get("status"));
        assertTrue(response.getBody().get("timestamp") instanceof LocalDateTime);
    }

    @Test
    void testHandleGeneralException() {
        // Given
        Exception exception = new Exception("Error inesperado");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleGeneralException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Ocurrió un error inesperado", response.getBody().get("message"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().get("status"));
        assertTrue(response.getBody().get("timestamp") instanceof LocalDateTime);
    }
}

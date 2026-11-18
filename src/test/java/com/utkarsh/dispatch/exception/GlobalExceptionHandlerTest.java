package com.utkarsh.dispatch.exception;

import com.utkarsh.dispatch.dto.ApiResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();


    @Test
    void shouldReturnBadRequestForMalformedJson() {

        // Arrange
        HttpMessageNotReadableException exception =
                mock(HttpMessageNotReadableException.class);

        // Act
        var response = handler.handleInvalidJson(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals(
                "Invalid JSON request. Please check your request body.",
                response.getBody().getMessage()
        );
    }

    @Test
    void shouldReturnBadRequestWithValidationMessage() {

        // Arrange
        FieldError fieldError = new FieldError(
                "orderRequestDTO",
                "orderId",
                "Order ID is required"
        );

        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        // Act
        var response = handler.handleValidationException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals("error", response.getBody().getStatus());

        assertEquals(
                "orderId: Order ID is required",
                response.getBody().getMessage()
        );
    }

    @Test
    void shouldReturnBadRequestForValidationException() {

        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of());

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult())
                .thenReturn(bindingResult);

        var response = handler.handleValidationException(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        assertEquals("error", response.getBody().getStatus());

        assertEquals("", response.getBody().getMessage());
    }

    @Test
    void shouldReturnBadRequestForIllegalArgumentException() {

        IllegalArgumentException exception =
                new IllegalArgumentException("Duplicate order ID");

        var response =
                handler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Duplicate order ID", response.getBody().getMessage());
    }

    @Test
    void shouldReturnInternalServerErrorForUnexpectedException() {

        Exception exception =
                new RuntimeException("Database connection failed");

        var response =
                handler.handleGeneralException(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals("error", response.getBody().getStatus());

        assertEquals(
                "Something went wrong. Please try again later.",
                response.getBody().getMessage()
        );
    }

}
package com.utkarsh.dispatch.exception;

import com.utkarsh.dispatch.dto.ApiResponseDTO;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

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
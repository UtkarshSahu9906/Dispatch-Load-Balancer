package com.utkarsh.dispatch;

import com.utkarsh.dispatch.dto.ApiResponseDTO;
import com.utkarsh.dispatch.util.DistanceCalculator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@SpringBootApplication
public class DispatchLoadBalancerApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				DispatchLoadBalancerApplication.class,
				args
		);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseDTO> handleValidationException(
			MethodArgumentNotValidException exception
	) {

		String errorMessage = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));

		ApiResponseDTO response = new ApiResponseDTO(
				errorMessage,
				"error"
		);

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(response);
	}
}

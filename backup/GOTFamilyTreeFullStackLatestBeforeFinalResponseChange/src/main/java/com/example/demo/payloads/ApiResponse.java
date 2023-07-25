package com.example.demo.payloads;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ApiResponse {

	private String message;
	
	private HttpStatus status;
	
	private boolean success;
}

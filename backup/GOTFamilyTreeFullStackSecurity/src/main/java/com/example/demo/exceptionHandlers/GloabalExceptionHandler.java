package com.example.demo.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.customExceptions.CharacterForIdNotFoundException;
import com.example.demo.customExceptions.NoCharactersAvailableInDbException;
import com.example.demo.payloads.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GloabalExceptionHandler {

	@ExceptionHandler(NoCharactersAvailableInDbException.class)
	public ResponseEntity<ApiResponse> handleNoCharactersAvailableInDbException(NoCharactersAvailableInDbException ex){
		log.info("handling NoCharactersAvailableInDbException...");
		
		ApiResponse res =  ApiResponse.builder()
				.message(ex.getMessage())
				.status(HttpStatus.NO_CONTENT)
				.success(false)
				.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
	
	@ExceptionHandler(CharacterForIdNotFoundException.class)
	public ResponseEntity<ApiResponse> handleCharacterForIdNotFoundException(CharacterForIdNotFoundException ex){
		
		log.info("handling CharacterForIdNotFoundException...");
		
		ApiResponse res =  ApiResponse.builder()
				.message(ex.getMessage())
				.status(HttpStatus.NOT_FOUND)
				.success(false)
				.build();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		
	}
}

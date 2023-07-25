package com.example.demo.customExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharacterForIdNotFoundException extends RuntimeException {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CharacterForIdNotFoundException(String msg) {
		super(msg);
		log.info("throwing CharacterForIdNotFoundException ...");
	}
}

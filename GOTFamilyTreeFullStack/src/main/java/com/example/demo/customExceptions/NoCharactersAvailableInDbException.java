package com.example.demo.customExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoCharactersAvailableInDbException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  NoCharactersAvailableInDbException(String msg) {
		super(msg);
		log.info("throwing NoCharactersAvailableInDbException");
	}
}

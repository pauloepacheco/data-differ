package com.waes.data.differ.exception;

/**
 * The InvalidRequestException is the unchecked exception class to handle validation errors.
 * 
 * @author Paulo Pacheco
 */
@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException{

	public InvalidRequestException() {
	}
	
	public InvalidRequestException(String message) {
		super(message);
	}
	
	public InvalidRequestException(Throwable cause) {
		super(cause);
	}
	
	public InvalidRequestException(String message, Throwable cause) {
		super(message,cause);
	}
}

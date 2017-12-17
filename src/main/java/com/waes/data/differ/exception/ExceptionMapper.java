package com.waes.data.differ.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.waes.data.differ.response.DifferExceptionResponse;
import com.waes.data.differ.response.DifferExceptionResponse.ErrorType;

/**
 * The class ExceptionMapper represents a global exception handler. 
 * It allows mapping of several exceptions to the same method to be handled together if needed.
 * 
 * @author Paulo Pacheco
 */
@ControllerAdvice
public class ExceptionMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapper.class); 
	
	/**
	 * Exception handler for the MethodArgumentNotValidException class
	 * 
	 * @param ex The custom Invalid Request Exception class
	 * @return Returns a custom JSON Payload containing the errors along with a 400 as HTTP request 
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<DifferExceptionResponse> handleInvalidRequestException(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<String>();
		BindingResult bindingResult = ex.getBindingResult();
        
        bindingResult.getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .forEach(e -> {
        		errors.add(e);
        		LOGGER.error("Invalid request. Error message{}", e);
        	});
        final DifferExceptionResponse response = new DifferExceptionResponse(
        		ErrorType.VALIDATION_ERROR.getDescription(), errors);
        
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	/**
	 * Exception handler for the InvalidRequestException class
	 * 
	 * @param ex The custom Invalid Request Exception class
	 * @return Returns a custom JSON Payload containing the error along with a 400 as HTTP request 
	 */
	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<DifferExceptionResponse> handleInvalidRequestException(InvalidRequestException ex) {
		LOGGER.error("Invalid request. Error message{}", ex.getMessage());
		final DifferExceptionResponse response = new DifferExceptionResponse(ErrorType.VALIDATION_ERROR.getDescription(), 
				Arrays.asList(ex.getMessage()));
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	/**
	 * Exception handler for the Throwable class. This method is intended to caught errors that were not handled by the application
	 * 
	 * @param ex The custom Invalid Request Exception class
	 * @return Returns a custom JSON Payload containing the error along with a 500 as HTTP request 
	 */
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<DifferExceptionResponse> handleUnknownException(Throwable ex) {
		LOGGER.error("Invalid request. Error message{}", ex.getMessage(), ex);
		final DifferExceptionResponse response = new DifferExceptionResponse(ErrorType.TECHNICAL_ERROR.getDescription(), 
				Arrays.asList("A technical error has occurred"));
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}

package com.waes.data.differ.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The DifferExceptionResponse is a wrapper class to provide detailed 
 * information when an exception is thrown at runtime
 * 
 * @author Paulo Pacheco
 */
@JsonInclude(Include.NON_EMPTY)
public class DifferExceptionResponse {

	@JsonProperty("error_category")
	private String errorType;
	
	@JsonProperty("errors")
	private List<String> errors;
	
	public DifferExceptionResponse() {
	}
	
	public DifferExceptionResponse(String errorType, List<String> errors) {
		super();
		this.errorType = errorType;
		this.errors = errors;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public enum ErrorType {
		
		VALIDATION_ERROR("Validation Error"),
		TECHNICAL_ERROR("Technical Error"),
		BUSINESS_ERROR("Business Validation");
		
		private final String description;
		
		private ErrorType(final String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
}

package com.waes.data.differ.request;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The DifferRequest is a wrapper class to receive the JSON request in the the REST controller.
 * Uses Hibernate Validator to avoid validating field by field manually 
 *  
 * @author Paulo Pacheco
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DifferRequest {

	@NotBlank(message="encoded_data is mandatory")
	@JsonProperty("encoded_data")
	private String encodedData;

	public DifferRequest() {
	}
	
	public DifferRequest(String encodedData) {
		super();
		this.encodedData = encodedData;
	}

	public String getEncodedData() {
		return encodedData;
	}

	public void setEncodedData(String encodedData) {
		this.encodedData = encodedData;
	}

	@Override
	public String toString() {
		return "DataDifferRequest [encodedData=" + encodedData + "]";
	}
}

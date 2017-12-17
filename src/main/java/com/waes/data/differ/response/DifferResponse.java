package com.waes.data.differ.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The DifferResponse class is a wrapper class to provided the result details of a diff operation
 * 
 * @author Paulo Pacheco
 */
@JsonInclude(Include.NON_EMPTY)
public class DifferResponse {

	@JsonProperty("message")
	private ResponseStatus responseStatus;
	
	@JsonProperty("result")
	private Set<OffSetLengthResponse> offSetLengthResponse;

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
	
	public DifferResponse() {
	}
	
	public DifferResponse(ResponseStatus responseStatus, Set<OffSetLengthResponse> offSetLengthResponse) {
		super();
		this.responseStatus = responseStatus;
		this.offSetLengthResponse = offSetLengthResponse;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Set<OffSetLengthResponse> getOffSetLengthResponse() {
		return offSetLengthResponse;
	}

	public void setOffSetLengthResponse(Set<OffSetLengthResponse> offSetLengthResponse) {
		this.offSetLengthResponse = offSetLengthResponse;
	}

	public static class OffSetLengthResponse {
		
		private int offset;
		private int length;
		
		public OffSetLengthResponse() {}
		
		public OffSetLengthResponse(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
	}
	
	public enum ResponseStatus{
		EQUAL,
		NOT_EQUAL_SIZE,
		CONTENT_DOES_NOT_MATCH
	}
}

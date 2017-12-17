package com.waes.data.differ.controller;

import java.util.Base64;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.waes.data.differ.entity.DataDifferEntity.RequestSource;
import com.waes.data.differ.exception.InvalidRequestException;
import com.waes.data.differ.request.DifferRequest;
import com.waes.data.differ.response.DifferResponse;
import com.waes.data.differ.service.DifferService;

/**
 * The DifferController class provides HTTP Endpoints to store the received base64 JSON for comparison.
 * 
 * The supported HTTP response status are :
 * 201 - Created - Indicates that the record was successfully created
 * 200 - Success - Indicates that the request was processed successfully
 * 400 - Bad Request - Indicates that there is/are error(s) with the provided data for request
 * 500 - Internal Error - Indicates that an unexpected error has occurred
 * 
 * @author Paulo Pacheco
 *
 */
@RestController
@RequestMapping("/v1/diff")
public class DifferController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DifferController.class);

	@Autowired
	@Qualifier("differService")
	private DifferService differService;
	
	/**
	 * Stores the data received thru a HTTP PUT request on the left Endpoint for
	 * comparison
	 * 
	 * @param id The id to store the received data in Database
	 * @param data The base64 JSON data for comparison
	 * @return Returns a 201 HTTP status code in case of success
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/left", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE }, 
		produces = { MediaType.APPLICATION_JSON_VALUE })	
	public void saveLeft(@PathVariable("id")String id, @Valid @RequestBody DifferRequest leftPayload) {
		LOGGER.debug("Received left request id{} and payload{}", id, leftPayload);
		if(!StringUtils.isNumeric(id)) {
			throw new InvalidRequestException("Invalid ID");			
		}
		differService.processRequest(Long.valueOf(id), decodeBase64Data(leftPayload.getEncodedData()), RequestSource.LEFT);
	}

	/**
	 * Stores the data received thru a HTTP PUT request on right Endpoint for
	 * comparison
	 * 
	 * @param id The id to store the received data in Database
	 * @param data The base64 JSON data for comparison
	 * @return Returns a 201 HTTP status code in case of success
	 */
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/{id}/right", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE }, 
		produces = { MediaType.APPLICATION_JSON_VALUE })
	public void saveRight(@PathVariable("id") String id, @Valid @RequestBody DifferRequest rightPayload){
		LOGGER.debug("Received right request id{} and payload{}", id, rightPayload);
		if(!StringUtils.isNumeric(id)) {
			throw new InvalidRequestException("Invalid ID");			
		}
		differService.processRequest(Long.valueOf(id), decodeBase64Data(rightPayload.getEncodedData()), RequestSource.RIGHT);
	}

	/**
	 * Gets the received data and apply comparison logic
	 * 
	 * @param id The id to find the element
	 * @return Returns a 200 as HTTP status and the JSON element containing the result set for a given comparison
	 */
	@RequestMapping(value = "/{id}", method=RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DifferResponse> getDifference(@PathVariable("id") String id) {
		LOGGER.debug("Start performing diff for id{}", id);
		if(!StringUtils.isNumeric(id)) {
			throw new InvalidRequestException("Invalid ID");			
		}
		return ResponseEntity.ok(differService.getDiff(Long.valueOf(id)));
	}
	
	/**
	 * Decodes the received base64 JSON Payload from HTTP Endpoints
	 * 
	 * @param base64Data The String containing the data in the base64 encoded format
	 * @return The decoded byte array of a base64 String
	 */
	private static byte[] decodeBase64Data(final String base64Data) throws InvalidRequestException{
		if (StringUtils.isEmpty(base64Data)) return null;
		try {
			return Base64.getDecoder().decode(base64Data);
		}catch (IllegalArgumentException e) {
			throw new InvalidRequestException("Invalid base64", e);
		}
	}
}

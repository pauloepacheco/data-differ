package com.waes.data.differ.service;

import com.waes.data.differ.entity.DataDifferEntity.RequestSource;
import com.waes.data.differ.response.DifferResponse;

/**
 * The DifferService represents the service layer to make the communication 
 * between the Rest Controller and Repository
 * 
 * @author Paulo Pacheco
 */
public interface DifferService {
	
	/**
	 * Process the received request and store the data in the Database
	 * 
	 * @param id The id to store the received data
	 * @param data The data received thru the Rest Controller
	 * @param source The type of operation
	 */
	public void processRequest(long id, byte[] data, RequestSource source);
	
	/**
	 * Calculates the difference between the reiceved data in both Rest endpoints
	 * 
	 * @param id The id to process the data
	 * @return Returns a wrapper class that represents the details information of the differences
	 */
	public DifferResponse getDiff(long id);
}

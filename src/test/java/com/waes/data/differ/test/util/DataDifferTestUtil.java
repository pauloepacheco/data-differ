package com.waes.data.differ.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Paulo Pacheco 
 */
public class DataDifferTestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataDifferTestUtil.class);
	
	public static String getJsonFromPojo(Object obj) {
		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			LOGGER.error("An error has occurred when trying to convert Pojo to JSON", e);
		}
		return json;
	}
}

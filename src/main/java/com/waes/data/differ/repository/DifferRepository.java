package com.waes.data.differ.repository;

import java.util.Optional;

import com.waes.data.differ.entity.DataDifferEntity;

/**
 * The DifferRepository represents a repository class to perform data access tasks
 * 
 * @author Paulo Pacheco
 */
public interface DifferRepository {
	
	/**
	 * Finds the record by id
	 * 
	 * @param id The id to find the record in the Database
	 * @return Returns an Optional of DataDiffEntity to avoid NPE runtime exception
	 */
	public Optional<DataDifferEntity> findById(long id);
	
	/**
	 * Persists/Update the object in Database based on id.
	 * 
	 * @param entity The DataDifferEntity to be persisted/updated
	 */
	public void save(final DataDifferEntity entity);
}

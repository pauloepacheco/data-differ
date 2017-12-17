package com.waes.data.differ.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.waes.data.differ.entity.DataDifferEntity;

/**
 * The DifferRepositoryImpl class is intended to perform Database operations over the 
 * DifferDataIntegration entity.
 * The transaction is not created at this point to avoid multiple transactions being created by the same service.
 * 
 * @author Paulo Pacheco
 */
@Repository("differDao")
public class DifferRepositoryImpl implements DifferRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Loads the DataDifferEntity from Database based on id and 
	 * returns a Optional of DataDifferEntity
	 */
	@Override
	public Optional<DataDifferEntity> findById(long id) {
		return Optional.ofNullable(entityManager.find(DataDifferEntity.class, id));
	}
	
	/**
	 * Save/Update the DataDifferEntity in Database based on id
	 */
	@Override
	public void save(DataDifferEntity entity) {
		entityManager.merge(entity);
	}
}

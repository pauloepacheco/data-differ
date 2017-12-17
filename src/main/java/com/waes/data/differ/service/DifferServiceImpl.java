package com.waes.data.differ.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.waes.data.differ.entity.DataDifferEntity;
import com.waes.data.differ.entity.DataDifferEntity.RequestSource;
import com.waes.data.differ.exception.InvalidRequestException;
import com.waes.data.differ.repository.DifferRepository;
import com.waes.data.differ.response.DifferResponse;
import com.waes.data.differ.response.DifferResponse.OffSetLengthResponse;
import com.waes.data.differ.response.DifferResponse.ResponseStatus;

/**
 * The DifferServiceImpl represents the Service layer to perform operations 
 * between the REST API and data access module
 * 
 * @author Paulo Pacheco
 */

@Service("differService")
public class DifferServiceImpl implements DifferService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DifferServiceImpl.class);
	
	@Autowired
	@Qualifier("differDao")
	private DifferRepository differRepository;

	/**
	 * Process the request and store the received data in the DB.
	 * Propagation Required is used to open a transaction with the database and 
	 * to avoid creating transactions when there is one in progress. 
	 * In case of an exception the data won't be stored in the database. A Rollback operaion will be performed  
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processRequest(long id, byte[] data, RequestSource source) {

		LOGGER.debug("Processing request for{}", source);

		Optional<DataDifferEntity> dataDiffer = differRepository.findById(id);
		DataDifferEntity entity = dataDiffer.orElse(new DataDifferEntity(id));

		if (RequestSource.LEFT.equals(source)) 
			entity.setLeft(data);
		else
			entity.setRight(data);
		
		differRepository.save(entity);
	}
	/**
	 * Process the difference between the received data in both REST Endpoints. 
	 * Propagation Required is used to open a transaction with the database and 
	 * to avoid creating transactions when there is one in progress.
	 * 
	 * Returns a wrapper class that contains the result of the diff operation
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public DifferResponse getDiff(long id) {

		Optional<DataDifferEntity> dataDiffer = differRepository.findById(id);
		dataDiffer.orElseThrow(() -> new InvalidRequestException("The provided ID does not exist"));
		
		final DataDifferEntity entity = dataDiffer.get();
		final DifferResponse response = new DifferResponse();
		
		int leftSize = entity.getLeft() != null ? entity.getLeft().length : 0;
		int rightSize = entity.getRight() != null ? entity.getRight().length : 0;

		if (Arrays.equals(entity.getLeft(), entity.getRight())) {
			response.setResponseStatus(ResponseStatus.EQUAL);

		} else if (leftSize != rightSize) {
			response.setResponseStatus(ResponseStatus.NOT_EQUAL_SIZE);

		} else if (leftSize == rightSize) {

			//calculates the length and offset of the differences found between the two received data
			Set<OffSetLengthResponse> offSetLengthResponse = new HashSet<OffSetLengthResponse>();
			for (int i = 0; i < leftSize; i++) {
				if (entity.getLeft()[i] != entity.getRight()[i]) {
					int offset = i;
					int length = 0;
					
					//the elements are different at this point so increment the length variable and go to the 
					//next position to determine if it is different to continue incrementing the length variable
					lengthCalculator: 
					for (int j = i; j < leftSize; j++) {
						i = j;
						if (entity.getLeft()[j] != entity.getRight()[j]) length++;
						else break lengthCalculator;
					}
					offSetLengthResponse.add(new OffSetLengthResponse(offset, length));
				}
			}
			response.setResponseStatus(ResponseStatus.CONTENT_DOES_NOT_MATCH);
			response.setOffSetLengthResponse(offSetLengthResponse);
		}

		return response;
	}
}

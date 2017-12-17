package com.waes.data.differ.test.business;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.waes.data.differ.entity.DataDifferEntity;
import com.waes.data.differ.repository.DifferRepository;
import com.waes.data.differ.response.DifferResponse;
import com.waes.data.differ.response.DifferResponse.OffSetLengthResponse;
import com.waes.data.differ.response.DifferResponse.ResponseStatus;
import com.waes.data.differ.service.DifferService;
import com.waes.data.differ.service.DifferServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The class DataDifferBusinessTest represents the test suite cases for  
 * business implementations that are available thru the REST API 
 * 
 * @author Paulo Pacheco
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DataDifferBusinessTest {

	@Mock
	private DifferRepository mockDifferRepository;
	
	@InjectMocks
	private DifferService differService = new DifferServiceImpl();
	
	@Test
	public final void test_performDiff_and_validate_returned_offsets_when_same_size_and_content_does_not_match() {
		DataDifferEntity entity = new DataDifferEntity(1L);
		entity.setLeft(new byte[] {1,2,3,4,5,6});
		entity.setRight(new byte[] {1,2,2,4,5,6});
		
		doReturn(Optional.of(entity)).when(mockDifferRepository).findById(anyLong());
		DifferResponse response = differService.getDiff(1L);
		
		@SuppressWarnings("serial")
		Set<OffSetLengthResponse> offSetLengthResponse = new HashSet<OffSetLengthResponse>() {{
			add(new OffSetLengthResponse(2, 1));
		}};
		
		assertEquals(offSetLengthResponse.stream().findFirst().get().getLength(), 
				response.getOffSetLengthResponse().stream().findFirst().get().getLength());
		
		assertEquals(offSetLengthResponse.stream().findFirst().get().getOffset(), 
				response.getOffSetLengthResponse().stream().findFirst().get().getOffset());
	}
	
	@Test
	public final void test_performDiff_and_return_notEqualSize_when_fields_have_different_size() {
		DataDifferEntity entity = new DataDifferEntity();
		entity.setLeft("cGF1bG8".getBytes());
		entity.setRight("cGFjaGVjbw==".getBytes());
		doReturn(Optional.of(entity)).when(mockDifferRepository).findById(anyLong());
		assertEquals(ResponseStatus.NOT_EQUAL_SIZE, differService.getDiff(anyLong()).getResponseStatus());
		assertTrue(entity.getLeft().length != entity.getRight().length);
	}
	
	@Test
	public final void test_performDiff_and_return_equal_when_fields_have_same_size() {
		DataDifferEntity entity = new DataDifferEntity();
		entity.setLeft("cGF1bG8=".getBytes());
		entity.setRight("cGF1bG8=".getBytes());
		doReturn(Optional.of(entity)).when(mockDifferRepository).findById(anyLong());
		assertEquals(ResponseStatus.EQUAL, differService.getDiff(anyLong()).getResponseStatus());
		assertTrue(Arrays.equals(entity.getLeft(), entity.getLeft()));
	}
	
	@Test
	public final void testPerformDiff_and_return_contentDoesNotMatch_when_fields_have_same_size_but_content_is_different() {
		DataDifferEntity entity = new DataDifferEntity();
		entity.setLeft("cGF1bG8=".getBytes());
		entity.setRight("MTExMTE=".getBytes());
		doReturn(Optional.of(entity)).when(mockDifferRepository).findById(anyLong());
		assertEquals(ResponseStatus.CONTENT_DOES_NOT_MATCH, differService.getDiff(anyLong()).getResponseStatus());
		assertTrue(!Arrays.equals(entity.getLeft(), entity.getRight()));
	}
}

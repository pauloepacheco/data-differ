package com.waes.data.differ.test.integration;

import static com.waes.data.differ.test.util.DataDifferTestUtil.getJsonFromPojo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.waes.data.differ.controller.DifferController;
import com.waes.data.differ.exception.ExceptionMapper;
import com.waes.data.differ.repository.DifferRepository;
import com.waes.data.differ.request.DifferRequest;
import com.waes.data.differ.response.DifferExceptionResponse.ErrorType;
import com.waes.data.differ.service.DifferService;

/**
 * The DataDifferExceptionTest represents the test suite cases for the
 * ExceptionWrapper that is based on ControllerAdvice to handle unchecked exceptions
 * 
 * @author Paulo Pacheco
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DataDifferExceptionTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	private DifferController differController;

	@Mock
	private DifferService mockDifferService;

	@Mock
	private DifferRepository mockDifferRepository;
	
	@Before
	public final void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(differController)
		         .setControllerAdvice(new ExceptionMapper()).build();
	}
	
	@Test
	public final void test_saveLeftRequest_and_return_invalid_request_exception_when_id_is_invalid() throws Exception {
		mockMvc.perform(
				put("/v1/diff/LEFT_ID/left").content(getJsonFromPojo(new DifferRequest("bGVmdF9lbmRwb2ludA==")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error_category", Matchers.is(ErrorType.VALIDATION_ERROR.getDescription())));
	}
	
	@Test
	public final void test_saveRightRequest_and_return_invalid_request_exception_when_id_Is_invalid() throws Exception {
		mockMvc.perform(
				put("/v1/diff/RIGHT_ID/right").content(getJsonFromPojo(new DifferRequest("cmlnaHRfZW5kcG9pbnQ=")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error_category", Matchers.is(ErrorType.VALIDATION_ERROR.getDescription())));
	}
	
	@Test
	public final void testSaveRightRequest_and_return_invalid_request_exception_when_payload_is_null() throws Exception {
		mockMvc.perform(
				put("/v1/diff/1/right").content(getJsonFromPojo(new DifferRequest())).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error_category", Matchers.is(ErrorType.VALIDATION_ERROR.getDescription())))
				.andExpect(jsonPath("$.errors[0]", Matchers.is("encoded_data is mandatory")));
	}
	
	@Test
	public final void test_saveRightRequest_and_return_invalid_request_exception_when_payload_is_blank() throws Exception {
		mockMvc.perform(
				put("/v1/diff/1/right").content(getJsonFromPojo(new DifferRequest(""))).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error_category", Matchers.is(ErrorType.VALIDATION_ERROR.getDescription())))
				.andExpect(jsonPath("$.errors[0]", Matchers.is("encoded_data is mandatory")));
	}
}

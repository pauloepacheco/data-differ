package com.waes.data.differ.test.integration;

import static com.waes.data.differ.test.util.DataDifferTestUtil.getJsonFromPojo;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import com.waes.data.differ.entity.DataDifferEntity;
import com.waes.data.differ.repository.DifferRepository;
import com.waes.data.differ.request.DifferRequest;
import com.waes.data.differ.response.DifferResponse;
import com.waes.data.differ.response.DifferResponse.ResponseStatus;
import com.waes.data.differ.service.DifferService;

/**
 * The DifferRestControllerTest represents the test suite cases for the DifferRestController REST API.
 * 
 * @author Paulo Pacheco
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DifferRestControllerTest {

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
		this.mockMvc = MockMvcBuilders.standaloneSetup(differController).build();
	}

	@Test
	public final void testSaveRightEndpointAndReturn201HttpStatusWhenSuccessfull() throws Exception {
		doReturn(Optional.empty()).when(mockDifferRepository).findById(anyLong());
		final DifferRequest request = new DifferRequest("cGF1bG8=");
		mockMvc.perform(
				put("/v1/diff/1/right").content(getJsonFromPojo(request)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public final void testSaveLeftEndpointAndReturn201HttpStatusWhenSuccessfull() throws Exception {
		doReturn(Optional.empty()).when(mockDifferRepository).findById(anyLong());
		final DifferRequest request = new DifferRequest("cGFjaGVjbw==");
		mockMvc.perform(
				put("/v1/diff/2/left").content(getJsonFromPojo(request)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void testGetDiffWhenRequestsAreEqualAndReturn200HttpStatus() throws Exception {
		doReturn(Optional.of(new DataDifferEntity(1L, "UGF1bG9QYWNoZWNv".getBytes(), "UGF1bG9QYWNoZWNv".getBytes())))
				.when(mockDifferRepository).findById(anyLong());
		doReturn(new DifferResponse(ResponseStatus.EQUAL, null)).when(mockDifferService).getDiff(anyLong());
		mockMvc.perform(get("/v1/diff/1"))
				.andExpect(jsonPath("$.message", Matchers.is(ResponseStatus.EQUAL.toString())));
	}

	@Test
	public void testGetDiffWhenRequestsAreNotEqualAndSizeIsDifferentThanReturn200HttpStatus() throws Exception {
		doReturn(Optional.of(new DataDifferEntity(1L, "PauloPacheco1".getBytes(), "UGF1bG9QYWNoZWNv".getBytes())))
				.when(mockDifferRepository).findById(anyLong());
		doReturn(new DifferResponse(ResponseStatus.NOT_EQUAL_SIZE, null)).when(mockDifferService).getDiff(anyLong());
		mockMvc.perform(get("/v1/diff/1"))
				.andExpect(jsonPath("$.message", Matchers.is(ResponseStatus.NOT_EQUAL_SIZE.toString())));
	}

	@Test
	public void testGetDiffWhenRequestsAreNotEqualAndSameThanReturn200HttpStatus() throws Exception {
		doReturn(Optional.of(new DataDifferEntity(1L, "UGF1bG9QYWNoZWNv".getBytes(), "UGF1bG9QYWNoZWMx".getBytes())))
				.when(mockDifferRepository).findById(anyLong());
		doReturn(new DifferResponse(ResponseStatus.CONTENT_DOES_NOT_MATCH, null)).when(mockDifferService)
				.getDiff(anyLong());
		mockMvc.perform(get("/v1/diff/1"))
				.andExpect(jsonPath("$.message", Matchers.is(ResponseStatus.CONTENT_DOES_NOT_MATCH.toString())));
	}

	@Test
	public final void testUpdateLeftEndpointAndReturn201HttpStatusWhenSuccessfull() throws Exception {
		doReturn(Optional.of(new DataDifferEntity(3L))).when(mockDifferRepository).findById(anyLong());
		final DifferRequest request = new DifferRequest("cGF1bG8=");
		mockMvc.perform(
				put("/v1/diff/3/left").content(getJsonFromPojo(request)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public final void testUpdateRightEndpointAndReturn201HttpStatusWhenSuccessfull() throws Exception {
		doReturn(Optional.of(new DataDifferEntity(4L))).when(mockDifferRepository).findById(anyLong());
		final DifferRequest request = new DifferRequest("cGF1bG8=");
		mockMvc.perform(
				put("/v1/diff/4/right").content(getJsonFromPojo(request)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}
}

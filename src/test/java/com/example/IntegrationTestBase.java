package com.example;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationTestBase {

	public final static UserDTO MOCK_USER_1 = UserDTO.builder().firstName("etunimi1").lastName("sukunimi1")
			.birthDate(LocalDate.now()).creationTime(LocalDateTime.now().minusDays(1)).build();

	public final static UserDTO MOCK_USER_2 = UserDTO.builder().firstName("etunimi2").lastName("sukunimi2")
			.birthDate(LocalDate.now()).creationTime(LocalDateTime.now().minusDays(2)).build();

	@Autowired
	private MockMvc mockMvc;

	public <T> T getRequestResponse(HttpMethod method, String url, Object requestBody, Class<T> clazz)
			throws Exception {
		return getRequestResponse(method, null, url, requestBody, null, clazz);
	}

	public <T> T getRequestResponse(HttpMethod method, String url, Object requestBody, String pathParam, Class<T> clazz)
			throws Exception {
		return getRequestResponse(method, null, url, requestBody, pathParam, clazz);
	}

	public <T> T getRequestResponse(HttpMethod method, HttpStatus status, String url, Object requestBody,
			String pathParam, Class<T> clazz) throws Exception {
		ResultActions resultActions = perform(method, status, url, requestBody, pathParam);
		return getObjectMapper().readValue(resultActions.andReturn().getResponse().getContentAsString(), clazz);
	}

	public void doRequest(HttpMethod method, HttpStatus status, String url, Object requestBody, String pathParam)
			throws Exception {
		perform(method, status, url, requestBody, pathParam);
	}

	private ResultActions perform(HttpMethod method, HttpStatus status, String url, Object requestBody,
			String pathParam) throws Exception {
		MockHttpServletRequestBuilder requestBuilder = StringUtils.isNotBlank(pathParam)
				? MockMvcRequestBuilders.request(method, url, pathParam)
				: MockMvcRequestBuilders.request(method, url);
		if (requestBody != null) {
			requestBuilder.content(toJsonString(requestBody)).contentType(MediaType.APPLICATION_JSON);
		}
		return this.mockMvc.perform(requestBuilder.accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status == null ? status().is2xxSuccessful() : status().is(status.value()));
	}

	private ObjectMapper objectMapper = null;

	private ObjectMapper getObjectMapper() {
		if (this.objectMapper == null) {
			this.objectMapper = new ObjectMapper();
		}
		return this.objectMapper;
	}

	private String toJsonString(final Object obj) {
		try {
			return getObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

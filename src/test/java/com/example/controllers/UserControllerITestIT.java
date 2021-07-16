package com.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.IntegrationTestBase;
import com.example.dto.UserDTO;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITestIT extends IntegrationTestBase {

	@Test
	public void testGetSavedUser() throws Exception {
		UserDTO savedUser = getRequestResponse(HttpMethod.POST, UserController.PATH_ROOT + UserController.PATH_SAVE_USER,
				MOCK_USER_1, UserDTO.class);

		UserDTO foundUser = getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_ID, null, String.valueOf(savedUser.getId()),
				UserDTO.class);

		assertAll(() -> assertThat(foundUser).isNotNull(), () -> assertThat(foundUser.getId()).isGreaterThan(0),
				() -> assertThat(foundUser.getFirstName()).isEqualToIgnoringCase(MOCK_USER_1.getFirstName()));
	}

	@Test
	public void testGetUsersByName() throws Exception {
//		List<UserDTO> response = Arrays.asList(getRequestResponse(HttpMethod.GET,
//				UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_ID, String.valueOf(savedUser.getId()), UserDTO[].class)));
//		UserDTO user = Optional.ofNullable(response).orElseGet(Lists::newArrayList).stream().findFirst().orElse(null);
//
//		assertAll(() -> assertThat(response).isNotNull().isNotEmpty().hasSize(1), () -> assertThat(user).isNotNull(),
//				() -> assertThat(user.getFirstName()).isEqualToIgnoringCase("Jaakko"));
	}

}

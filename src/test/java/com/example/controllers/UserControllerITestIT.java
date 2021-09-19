package com.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.IntegrationTestBase;
import com.example.dto.UserDTO;
import com.example.repositories.UserRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITestIT extends IntegrationTestBase {

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void init() {
		// reset the database
		this.userRepository.deleteAll();
	}

	@Test
	public void testGetUsers() throws Exception {
		// check that no users found initially
		List<UserDTO> emptySearch = Arrays.asList(getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_GET_USERS, null, null, null, UserDTO[].class));

		// save user and fetch it
		saveUser(MOCK_USER_1);
		List<UserDTO> foundUsers = Arrays.asList(getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_GET_USERS, null, null, null, UserDTO[].class));
		UserDTO foundUser = Optional.ofNullable(foundUsers).orElseGet(Lists::newArrayList).stream().findFirst()
				.orElse(null);
		assertAll(() -> assertThat(emptySearch).isEmpty(), () -> assertThat(foundUsers).isNotNull().hasSize(1),
				() -> assertThat(foundUser).isNotNull(), () -> assertThat(foundUser.getId()).isGreaterThan(0),
				() -> assertThat(foundUser.getFirstName()).isEqualToIgnoringCase(MOCK_USER_1.getFirstName()));
	}

	@Test
	public void testDeleteUser() throws Exception {
		UserDTO savedUser = saveUser(MOCK_USER_1);
		// get the saved user
		assertNotNull(getRequestResponse(HttpMethod.GET, UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_ID,
				null, null, String.valueOf(savedUser.getId()), UserDTO.class));
		// delete the saved user
		assertThat(getRequestResponse(HttpMethod.POST, UserController.PATH_ROOT + UserController.PATH_DELETE_USER, null,
				null, String.valueOf(savedUser.getId()), String.class))
						.isEqualToIgnoringCase(UserController.MSG_DELETE_USER_SUCCESS);
		// check the user is no longer found
		assertNull(getRequestResponse(HttpMethod.GET, UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_ID,
				null, null, String.valueOf(savedUser.getId()), UserDTO.class));
	}

	@Test
	public void testGetSavedUserById() throws Exception {
		UserDTO savedUser = saveUser(MOCK_USER_1);

		UserDTO foundUser = getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_ID, null, null,
				String.valueOf(savedUser.getId()), UserDTO.class);

		assertAll(() -> assertThat(foundUser).isNotNull(), () -> assertThat(foundUser.getId()).isGreaterThan(0),
				() -> assertThat(foundUser.getFirstName()).isEqualToIgnoringCase(MOCK_USER_1.getFirstName()));
	}

	@Test
	public void testGetSavedUserByName() throws Exception {
		UserDTO savedUser = saveUser(MOCK_USER_1);

		List<UserDTO> foundUsers = Arrays.asList(
				getRequestResponse(HttpMethod.GET, UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_NAME,
						null, ImmutableMap.of(UserController.PARAM_FIRST_NAME, savedUser.getFirstName(),
								UserController.PARAM_LAST_NAME, savedUser.getLastName()),
						null, UserDTO[].class));

		List<UserDTO> emptySearch = Arrays.asList(
				getRequestResponse(HttpMethod.GET, UserController.PATH_ROOT + UserController.PATH_GET_USER_BY_NAME,
						null, ImmutableMap.of(UserController.PARAM_FIRST_NAME, "Matti", UserController.PARAM_LAST_NAME,
								savedUser.getLastName()),
						null, UserDTO[].class));
		UserDTO foundUser = Optional.ofNullable(foundUsers).orElseGet(Lists::newArrayList).stream().findFirst()
				.orElse(null);

		assertAll(() -> assertThat(emptySearch).isEmpty(), () -> assertThat(foundUser).isNotNull(),
				() -> assertThat(foundUser.getId()).isGreaterThan(0),
				() -> assertThat(foundUser.getFirstName()).isEqualToIgnoringCase(MOCK_USER_1.getFirstName()));
	}

	@Test
	public void testGetSavedUserByBornBefore() throws Exception {
		UserDTO savedUser1 = saveUser(MOCK_USER_1);
		UserDTO savedUser2 = saveUser(MOCK_USER_2);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<UserDTO> foundUsers = Arrays.asList(getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_USERS_BORN_BEFORE, null,
				ImmutableMap.of(UserController.PARAM_DATE, formatter.format(LocalDate.now().minusDays(2))), null,
				UserDTO[].class));

		assertThat(foundUsers).isNotNull().isNotEmpty().hasSize(1);
		assertThat(foundUsers.get(0).getFirstName()).isEqualTo(savedUser1.getFirstName());

		foundUsers = Arrays.asList(getRequestResponse(HttpMethod.GET,
				UserController.PATH_ROOT + UserController.PATH_USERS_BORN_BEFORE, null,
				ImmutableMap.of(UserController.PARAM_DATE, formatter.format(LocalDate.now())), null, UserDTO[].class));

		assertThat(foundUsers).isNotNull().isNotEmpty().hasSize(2);
		assertThat(foundUsers.get(1).getFirstName()).isEqualTo(savedUser2.getFirstName());

		assertThat(getRequestResponse(HttpMethod.GET, UserController.PATH_ROOT + UserController.PATH_USERS_BORN_BEFORE,
				null, ImmutableMap.of(UserController.PARAM_DATE, formatter.format(LocalDate.now().minusDays(4))), null,
				UserDTO[].class)).isEmpty();
	}

	private UserDTO saveUser(UserDTO userDTO) throws Exception {
		UserDTO savedUser = getRequestResponse(HttpMethod.POST,
				UserController.PATH_ROOT + UserController.PATH_SAVE_USER, userDTO, null, UserDTO.class);
		assertAll(() -> assertThat(savedUser).isNotNull(), () -> assertThat(savedUser.getId()).isGreaterThan(0),
				() -> assertThat(savedUser.getFirstName()).isEqualToIgnoringCase(userDTO.getFirstName()));
		return savedUser;
	}

}

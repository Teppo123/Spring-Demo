package com.example.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.UserDTO;
import com.example.services.UserService;

@Controller
@RequestMapping(path = UserController.PATH_ROOT
//, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

	public static final String PATH_ROOT = "/user-controller"; // NOSONAR

	protected static final String PATH_GET_USER_BY_ID = "/user/{id}"; // NOSONAR
	protected static final String PATH_SAVE_USER = "/save-user"; // NOSONAR
	protected static final String PATH_GET_USERS = "/users";
	protected static final String PATH_GET_USER_BY_NAME = "/user-by-name";
	protected static final String PATH_USERS_BORN_BEFORE = "/users-born-before";
	protected static final String PATH_DELETE_USER = "/delete-user/{id}";

	protected static final String PARAM_ID = "id";
	protected static final String PARAM_FIRST_NAME = "firstName";
	protected static final String PARAM_LAST_NAME = "lastName";
	protected static final String PARAM_DATE = "date";
	
	protected static final String MSG_DELETE_USER_SUCCESS = "User deleted.";

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping(UserController.PATH_GET_USERS)
	public ResponseEntity<List<UserDTO>> getUsers() {
		LOGGER.info("entered /users");
		return ResponseEntity.ok(this.userService.getUsers());
	}

	@GetMapping(UserController.PATH_GET_USER_BY_ID)
	public ResponseEntity<UserDTO> getUser(@PathVariable(name = PARAM_ID) long id) {
		LOGGER.info(String.format("entered /users/%s", id));
		return ResponseEntity.ok(this.userService.getUserById(id));
	}

	@GetMapping(UserController.PATH_GET_USER_BY_NAME)
	public ResponseEntity<List<UserDTO>> getUserByName(@RequestParam(name = PARAM_FIRST_NAME) String firstName,
			@RequestParam(name = PARAM_LAST_NAME) String lastName) {
		LOGGER.info(String.format("entered /user-by-name/ with firstName = \"%s\" and lastName = \"%s\"", firstName,
				lastName));
		return ResponseEntity.ok(this.userService.getUsersByName(firstName, lastName));
	}

	@GetMapping(UserController.PATH_USERS_BORN_BEFORE)
	public ResponseEntity<List<UserDTO>> getUsersBornBefore(@RequestParam(name = PARAM_DATE) LocalDate date) {
		LOGGER.info(String.format("entered /user-born-before/ with %s", date));
		return ResponseEntity.ok(this.userService.getUsersBornBefore(date));
	}

	@PostMapping(path = UserController.PATH_SAVE_USER, consumes = MediaType.APPLICATION_JSON_VALUE
//			, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
	)
	public ResponseEntity<UserDTO> saveUser(@Validated @RequestBody UserDTO to) {
		LOGGER.info(String.format("entered /save-user/ with %s", to));
		return new ResponseEntity<>(this.userService.saveUser(to), HttpStatus.CREATED);
	}

//	@PostMapping(path = "/save-user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	public ResponseEntity<UserDTO> saveUserFormEncoded(UserDTO to) {
//		LOGGER.info(String.format("entered /save-user/ with %s", to));
//		return new ResponseEntity<>(this.userService.saveUser(to), HttpStatus.CREATED);
//	}

	@PostMapping(UserController.PATH_DELETE_USER)
	public ResponseEntity<String> deleteUser(@PathVariable(name = PARAM_ID) long id) {
		LOGGER.info(String.format("entered /delete-user/%s", id));
		try {
			this.userService.deleteUser(id);
			return ResponseEntity.ok(MSG_DELETE_USER_SUCCESS);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(String.format("User #%s not found.", id), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

package com.example.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.UserTO;
import com.example.services.UserService;

@Controller
@RequestMapping(UserController.ROOT_PATH)
public class UserController {

	public static final String ROOT_PATH = "/user-controller"; // NOSONAR

	protected static final String PARAM_ID = "id";
	protected static final String PARAM_FIRST_NAME = "firstName";
	protected static final String PARAM_LAST_NAME = "lastName";
	protected static final String PARAM_DATE = "date";

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<List<UserTO>> getUsers() {
		return ResponseEntity.ok(this.userService.getUsers());
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<UserTO> getUser(@PathVariable(name = PARAM_ID) long id) {
		return ResponseEntity.ok(this.userService.getUserById(id));
	}

	@GetMapping("/user-by-name")
	public ResponseEntity<UserTO> getUserByName(@RequestParam(name = PARAM_FIRST_NAME) String firstName,
			@RequestParam(name = PARAM_LAST_NAME) String lastName) {
		return ResponseEntity.ok(this.userService.getUserByName(firstName, lastName));
	}

	@GetMapping("/user-born-before")
	public ResponseEntity<List<UserTO>> getUsersBornBefore(@RequestParam(name = PARAM_DATE) LocalDate date) {
		return ResponseEntity.ok(this.userService.getUsersBornBefore(date));
	}

	@PostMapping("/save-user")
	public ResponseEntity<UserTO> saveUser(@Validated @RequestBody UserTO to) {
		return new ResponseEntity<>(this.userService.saveUser(to), HttpStatus.CREATED);
	}

}

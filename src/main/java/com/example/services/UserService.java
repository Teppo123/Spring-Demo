package com.example.services;

import java.time.LocalDate;
import java.util.List;

import com.example.dto.UserDTO;

public interface UserService {

	List<UserDTO> getUsers();

	UserDTO saveUser(UserDTO user);
	
	UserDTO getUserById(long id);
	
	List<UserDTO> getUsersByName(String firstName, String lastName);
	
	List<UserDTO> getUsersBornBefore(LocalDate date);

	void deleteUser(long id);
	
}

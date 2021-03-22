package com.example.services;

import java.time.LocalDate;
import java.util.List;

import com.example.dto.UserTO;

public interface UserService {

	List<UserTO> getUsers();

	UserTO saveUser(UserTO user);
	
	UserTO getUserById(long id);
	
	UserTO getUserByName(String firstName, String lastName);
	
	List<UserTO> getUsersBornBefore(LocalDate date);
	
}

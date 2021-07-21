package com.example.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.repositories.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAllByDeactivatedFalse();
	
	Optional<User> findByFirstNameAndLastNameAndDeactivatedFalse(String firstName, String lastName);
	
	List<User> findByFirstNameAndDeactivatedFalse(String firstName);
	
	List<User> findByLastNameAndDeactivatedFalse(String lastName);
	
	List<User> findByBirthDateBeforeAndDeactivatedFalse(Date date);
	
	Optional<User> findByIdAndDeactivatedFalse(long id);
	
}

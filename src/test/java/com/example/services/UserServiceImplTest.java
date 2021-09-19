package com.example.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.dto.UserDTO;
import com.example.repositories.UserRepository;
import com.example.repositories.entities.User;

/**
 * Checks that the methods of the service delegate correct calls to the
 * repository.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private User mockUser;

	@Mock
	private UserRepository mockUserRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	public void testGetUsers() {
		this.userService.getUsers();
		verify(this.mockUserRepository).findAllByDeactivatedFalse();
	}

	@Test
	public void testSaveuser() {
		UserDTO user = UserDTO.builder().birthDate(LocalDate.now()).build();
		this.userService.saveUser(user);
		verify(this.mockUserRepository).save(Mockito.any(User.class));
	}

	@Test
	public void testGetUserById() {
		long id = 1;
		this.userService.getUserById(id);
		verify(this.mockUserRepository).findByIdAndDeactivatedFalse(id);
	}

	@Test
	public void testGetUsersByName() {
		String firstName = "Etunimi";
		String lastName = "Sukunimi";
		this.userService.getUsersByName(firstName, lastName);
		verify(this.mockUserRepository).findByFirstNameAndLastNameAndDeactivatedFalse(firstName, lastName);
	}

	@Test
	public void testUsersBornBefore() {
		LocalDate localDate = LocalDate.now().minusWeeks(1);
		this.userService.getUsersBornBefore(localDate);
		verify(this.mockUserRepository).findByBirthDateBeforeAndDeactivatedFalse(Date.valueOf(localDate));
	}

	@Test
	public void testDeleteUser() {
		long id = 1;

		when(this.mockUserRepository.findById(id)).thenReturn(Optional.of(this.mockUser));

		this.userService.deleteUser(id);
		verify(this.mockUser).deactivate();
		verify(this.mockUserRepository).save(this.mockUser);
	}

}

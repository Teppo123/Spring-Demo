package com.example.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.UserDTO;
import com.example.dto.transformers.UserDTOTransformer;
import com.example.repositories.UserRepository;
import com.example.repositories.entities.User;
import com.example.repositories.entities.transformers.UserTransformer;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserDTO> getUsers() {
		return this.userRepository.findAllByDeactivatedFalse().stream().map(new UserDTOTransformer()::transform)
				.collect(Collectors.toList());
	}

	@Override
	public UserDTO saveUser(UserDTO to) {
		return Optional.ofNullable(to).map(new UserTransformer()::transform).map(this.userRepository::save)
				.map(new UserDTOTransformer()::transform).orElse(null);
	}

	@Override
	public UserDTO getUserById(long id) {
		return this.userRepository.findById(id).map(new UserDTOTransformer()::transform).orElse(null);
	}

	@Override
	public UserDTO getUserByName(String firstName, String lastName) {
		return this.userRepository.findByFirstNameAndLastNameAndDeactivatedFalse(firstName, lastName)
				.map(new UserDTOTransformer()::transform).orElse(null);
	}

	@Override
	public List<UserDTO> getUsersBornBefore(LocalDate date) {
		return this.userRepository.findByBirthDateBeforeAndDeactivatedFalse(Date.valueOf(date)).stream()
				.map(new UserDTOTransformer()::transform).collect(Collectors.toList());
	}

	@Override
	public void deleteUser(long id) throws EntityNotFoundException {
		deactivateUser(this.userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
	}

	private void deactivateUser(User user) {
		user.deactivate();
		this.userRepository.save(user);
	}

}

package com.example.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.UserTO;
import com.example.dto.transformers.UserTOTransformer;
import com.example.repositories.UserRepository;
import com.example.repositories.entities.transformers.UserTransformer;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<UserTO> getUsers() {
		return this.userRepository.findAllByDeactivatedFalse().stream().map(new UserTOTransformer()::transform)
				.collect(Collectors.toList());
	}

	@Override
	public UserTO saveUser(UserTO to) {
		return Optional.ofNullable(to).map(new UserTransformer()::transform).map(this.userRepository::save)
				.map(new UserTOTransformer()::transform).orElse(null);
	}

	@Override
	public UserTO getUserById(long id) {
		return this.userRepository.findById(id).map(new UserTOTransformer()::transform).orElse(null);
	}

	@Override
	public UserTO getUserByName(String firstName, String lastName) {
		return this.userRepository.findByFirstNameAndLastNameAndDeactivatedFalse(firstName, lastName)
				.map(new UserTOTransformer()::transform).orElse(null);
	}

	@Override
	public List<UserTO> getUsersBornBefore(LocalDate date) {
		return this.userRepository.findByBirthDateBeforeAndDeactivatedFalse(Date.valueOf(date)).stream()
				.map(new UserTOTransformer()::transform).collect(Collectors.toList());
	}

}

package com.example.repositories.entities.transformers;

import java.sql.Date;

import com.example.dto.UserDTO;
import com.example.repositories.entities.User;

public class UserTransformer {

	public User transform(UserDTO to) {
		return to == null ? null
				: User.builder().firstName(to.getFirstName()).lastName(to.getLastName())
						.birthDate(Date.valueOf(to.getBirthDate())).build();
	}

}

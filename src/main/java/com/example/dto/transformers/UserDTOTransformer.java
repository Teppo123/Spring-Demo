package com.example.dto.transformers;

import com.example.dto.UserDTO;
import com.example.repositories.entities.User;
import com.example.utils.CustomDateUtils;

public class UserDTOTransformer {

	public UserDTO transform(User entity) {
		return entity == null ? null
				: UserDTO.builder().birthDate(CustomDateUtils.toLocalDate(entity.getBirthDate()))
						.firstName(entity.getFirstName()).lastName(entity.getLastName())
						.creationTime(CustomDateUtils.toLocalDateTime(entity.getCreatedAt())).build();
	}

}

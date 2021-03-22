package com.example.dto.transformers;

import com.example.dto.UserTO;
import com.example.repositories.entities.User;
import com.example.utils.CustomDateUtils;

public class UserTOTransformer {

	public UserTO transform(User entity) {
		return entity == null ? null
				: UserTO.builder().birthDate(CustomDateUtils.toLocalDate(entity.getBirthDate()))
						.firstName(entity.getFirstName()).lastName(entity.getLastName())
						.creationTime(CustomDateUtils.toLocalDateTime(entity.getCreatedAt())).build();
	}

}

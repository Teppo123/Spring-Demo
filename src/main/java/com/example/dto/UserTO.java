package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTO implements Serializable {

	private static final long serialVersionUID = 176712563459609446L;

	@NotBlank(message = "First name can't be blank")
	private String firstName;

	@NotBlank(message = "Last name can't be blank")
	private String lastName;

	@NotNull(message = "Birth date can't be null")
	private LocalDate birthDate;

	private LocalDateTime creationTime;

}

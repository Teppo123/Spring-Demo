package com.example.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 176712563459609446L;
	
	private long id;

	@NotBlank(message = "First name can't be blank")
	private String firstName;

	@NotBlank(message = "Last name can't be blank")
	private String lastName;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message = "Birth date can't be null")
	private LocalDate birthDate;

//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime creationTime;

}

package com.example.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CustomDateUtils {

	private CustomDateUtils() {
	}

	public static LocalDate toLocalDate(Date dateToConvert) {
		return dateToConvert == null ? null
				: Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime toLocalDateTime(Date dateToConvert) {
		return dateToConvert == null ? null
				: dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

}

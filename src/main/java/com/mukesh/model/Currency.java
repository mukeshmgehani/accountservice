package com.mukesh.model;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.mukesh.exception.DataNotFoundException;

public enum Currency {
	GBP("GBP"), EUR("EUR"), INR("INR"),AUS("AUS"),USD("USD");

	private static Map<String, Currency> FORMAT_MAP = Stream.of(Currency.values())
			.collect(Collectors.toMap(s -> s.value.toUpperCase(), Function.identity()));

	private String value;

	Currency(String value) {
		this.value = value;
	}

	@JsonCreator // This is the factory method and must be static
	public static Currency fromString(String string) {
		return Optional.ofNullable(FORMAT_MAP.get(string.toUpperCase()))
				.orElseThrow(() -> new DataNotFoundException("Currency name --" + string
						+ "  is Invalid. It should contains value from this -- (GBP,EUR,INR,AUS,USD)"));
	}

	public String getValue() {
		return this.value;
	}
}

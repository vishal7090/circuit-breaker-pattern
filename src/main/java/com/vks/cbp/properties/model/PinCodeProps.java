package com.vks.cbp.properties.model;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rest-service.pin-code")
public record PinCodeProps(String baseUrl, Integer timeout) {

	public PinCodeProps {
		// Default base URL for the Pin Code API
		baseUrl = Optional.ofNullable(baseUrl).filter(s -> !s.isBlank()).orElse("https://api.postalpincode.in");

		// Default timeout in milliseconds (5 seconds)
		timeout = Optional.ofNullable(timeout).orElse(5000);
	}
}

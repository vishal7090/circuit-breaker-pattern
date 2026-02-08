package com.vks.cbp.exception.handler;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import com.vks.cbp.exception.InvalidPinCodeException;
import com.vks.cbp.exception.NoResponseException;
import com.vks.cbp.exception.NoSuchPinCodeException;
import com.vks.cbp.exception.NoSuchPostOfficeException;
import com.vks.cbp.exception.model.ErrorResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class PostalExceptionHandler {

	@ExceptionHandler({ NoSuchPinCodeException.class, NoSuchPostOfficeException.class, NoResponseException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Mono<ErrorResponse> handleNoFoundException(RuntimeException ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.NOT_FOUND, exchange);
	}

	@ExceptionHandler({ InvalidPinCodeException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Mono<ErrorResponse> handleInvalidException(RuntimeException ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, exchange);
	}

	// Handle JSON decoding issues (like your array/object mismatch)
	@ExceptionHandler(DecodingException.class)
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	public Mono<ErrorResponse> handleDecodingException(DecodingException ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.BAD_GATEWAY, exchange);
	}

	// Handle IllegalStateException (like block() usage)
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<ErrorResponse> handleIllegalStateException(IllegalStateException ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, exchange);
	}

	// Fallback for everything else
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<ErrorResponse> handleGenericException(Exception ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, exchange);
	}

	@ExceptionHandler(WebClientResponseException.class)
	public Mono<ErrorResponse> handleWebClientException(WebClientResponseException ex, ServerWebExchange exchange) {
		return buildErrorResponse(ex, HttpStatus.valueOf(ex.getStatusCode().value()), exchange);
	}

	private Mono<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, ServerWebExchange exchange) {
		ex.printStackTrace();
		ErrorResponse errorResponse = new ErrorResponse(Instant.now().toString(), status.value(),
				status.getReasonPhrase(), ex.getMessage(), exchange.getRequest().getPath().value());
		return Mono.just(errorResponse);
	}
}

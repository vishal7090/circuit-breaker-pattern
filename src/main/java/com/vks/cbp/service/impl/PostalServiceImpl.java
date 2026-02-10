package com.vks.cbp.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.vks.cbp.exception.InvalidPinCodeException;
import com.vks.cbp.exception.NoResponseException;
import com.vks.cbp.model.PinCodeResponse;
import com.vks.cbp.service.PostalService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostalServiceImpl implements PostalService {

	private final WebClient pinCodeWebClient;

	@Override
	@Retry(name = "searchPinCode")
	@CircuitBreaker(name = "searchPinCode", fallbackMethod = "searchPinCodeFallback")
	@Bulkhead(name = "searchPinCode", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "bulkheadFallback")
	@RateLimiter(name = "searchPinCode", fallbackMethod = "rateLimiterFallback")
	public Mono<PinCodeResponse> searchPinCode(String pinCode) {
		log.info("Searching for pin code: {}", pinCode);
		if (pinCode == null || pinCode.trim().isEmpty()) {
			throw new InvalidPinCodeException("Pin code must not be null or empty");
		}
		if (pinCode.trim().length() != 6) {
			throw new InvalidPinCodeException("Pin code must be 6 digits long");
		}
		return pinCodeWebClient.get().uri(uriBuilder -> uriBuilder.path("/pincode/{PINCODE}").build(pinCode)).retrieve()
				.bodyToFlux(PinCodeResponse.class).next()
				.switchIfEmpty(Mono.error(new NoResponseException("No response from API")));
	}

	public Mono<PinCodeResponse> searchPinCodeFallback(String pinCode, Throwable throwable) {
		PinCodeResponse response = new PinCodeResponse();
		if (throwable instanceof io.github.resilience4j.circuitbreaker.CallNotPermittedException) {
			response.setMessage("Circuit breaker is OPEN. Unable to fetch details for pin code: " + pinCode
					+ ". Please try again later.");
		} else {
			response.setMessage("Retry attempts exhausted for pin code: " + pinCode + ". Please try after some times.");
		}
		response.setStatus("Error");
		return Mono.just(response);
	}

	@Override
	@Retry(name = "searchPostOffice")
	@CircuitBreaker(name = "searchPostOffice", fallbackMethod = "searchPostOfficeFallback")
	@Bulkhead(name = "searchPostOffice", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "bulkheadFallback")
	@RateLimiter(name = "searchPostOffice", fallbackMethod = "rateLimiterFallback")
	public Mono<PinCodeResponse> searchPostOffice(String postOfficeName) {
		log.info("Searching for post office: {}", postOfficeName);
		return pinCodeWebClient.get().uri("postoffice/{POSTOFFICEBRANCHNAME}", postOfficeName).retrieve()
				.bodyToFlux(PinCodeResponse.class).next()
				.switchIfEmpty(Mono.error(new NoResponseException("No response from API")));
		// Alternative Way to Retry Handle
		// .retryWhen(reactor.util.retry.Retry.backoff(3, Duration.ofSeconds(2))
		// .filter(ex -> ex instanceof WebClientRequestException))
	}

	public Mono<PinCodeResponse> bulkheadFallback(String postOfficeName, Throwable throwable) {
		PinCodeResponse response = new PinCodeResponse();
		response.setStatus("Error");
		response.setMessage("Too many concurrent requests. Please try later.");
		return Mono.just(response);
	}

	public Mono<PinCodeResponse> rateLimiterFallback(String postOfficeName, Throwable throwable) {
		PinCodeResponse response = new PinCodeResponse();
		response.setStatus("Error");
		response.setMessage("Rate limit exceeded. Try after some time.");
		return Mono.just(response);
	}

	public Mono<PinCodeResponse> searchPostOfficeFallback(String postOfficeName, Throwable throwable) {
		PinCodeResponse response = new PinCodeResponse();
		if (throwable instanceof io.github.resilience4j.circuitbreaker.CallNotPermittedException) {
			response.setMessage("Circuit breaker is OPEN. Unable to fetch details for post office: " + postOfficeName
					+ ". Please try again later.");
		} else {
			response.setMessage(
					"Retry attempts exhausted for post office: " + postOfficeName + ". Please try after some times.");
		}
		response.setStatus("Error");
		return Mono.just(response);
	}

}

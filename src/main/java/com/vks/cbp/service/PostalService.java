package com.vks.cbp.service;

import com.vks.cbp.model.PinCodeResponse;

import reactor.core.publisher.Mono;

public interface PostalService {
	Mono<PinCodeResponse> searchPinCode(String pinCode);

	Mono<PinCodeResponse> searchPostOffice(String postOfficeName);

}

package com.vks.cbp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vks.cbp.model.PinCodeResponse;
import com.vks.cbp.service.PostalService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostalController {

	private final PostalService postalService;

	@GetMapping("/pincode/{pincode}")
	@Operation(summary = "Search PinCode", description = "Search details for a given pin code")
	public Mono<PinCodeResponse> searchPinCode(@PathVariable("pincode") String pinCode) {
		return postalService.searchPinCode(pinCode);
	}

	@GetMapping("/postoffice/{postOfficeName}")
	@Operation(summary = "Search PostOffice", description = "Search details for a given post office name")
	public Mono<PinCodeResponse> searchPostOffice(@PathVariable("postOfficeName") String postOfficeName) {
		return postalService.searchPostOffice(postOfficeName);
	}

}

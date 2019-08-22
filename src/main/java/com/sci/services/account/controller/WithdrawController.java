package com.sci.services.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.WebDepositPojo;
import com.sci.services.account.repository.WithdrawService;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class WithdrawController {
	private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawController.class);

	@Autowired
	WithdrawService withdrawService;

	/*
	 * @PostMapping public Mono<Status> saveWithdraw(@RequestBody WebDepositPojo
	 * user) { LOGGER.info("create: {}", user); Mono<WebDepositPojo> userMono =
	 * Mono.just(user); return withdrawService.saveWithdraw(userMono);
	 * 
	 * }
	 */
	@PostMapping
	public Mono<Status> saveWithdraw(
			@RequestBody WebDepositPojo user/* @RequestParam BigDecimal balance,@RequestParam String accountnumber */) {
		// LOGGER.info("create: {}", user);
		// Mono<WebDepositPojo> userMono = Mono.just(user);
		return withdrawService.saveWithdraw(user/* balance,accountnumber */);

	}

}

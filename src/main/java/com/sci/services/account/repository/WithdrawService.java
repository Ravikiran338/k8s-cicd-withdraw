package com.sci.services.account.repository;

import java.math.BigDecimal;

import com.sci.services.account.model.Status;
import com.sci.services.account.model.WebDepositPojo;

import reactor.core.publisher.Mono;

public interface WithdrawService {

	//public Mono<Status> saveWithdraw(Mono<WebDepositPojo> user);
	
	public Mono<Status> saveWithdraw(WebDepositPojo user/*BigDecimal balance,String accountnumber*/);
	
}

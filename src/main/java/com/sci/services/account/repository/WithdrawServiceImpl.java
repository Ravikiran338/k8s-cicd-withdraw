package com.sci.services.account.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.sci.services.account.constants.WithdrawQueryConstants;
import com.sci.services.account.constants.WithdrawServiceConstants;
import com.sci.services.account.model.Account;
import com.sci.services.account.model.Status;
import com.sci.services.account.model.TransactionPojo;
import com.sci.services.account.model.WebDepositPojo;
import com.sci.services.util.DatabaseUtil;
import com.sci.services.util.DepositServiceUtil;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import reactor.core.publisher.Mono;

@Service
public class WithdrawServiceImpl implements WithdrawService {

	private static DepositServiceUtil util = DepositServiceUtil.getInstance();

	@Override
	public Mono<Status> saveWithdraw(WebDepositPojo user/* BigDecimal balance,String accountnumber */) {
		Mono<WebDepositPojo> accountDetails = accountdetails(user/* balance,accountnumber */);
		Mono<TransactionPojo> transactionPojo = addTransactionInfo(accountDetails);
		Mono<Account> AccountPojo = getAccountInfo(transactionPojo);
		return updateAccountMono(AccountPojo);

	}

	private Mono<TransactionPojo> addTransactionInfo(Mono<WebDepositPojo> accountDetails) {
		Mono<TransactionPojo> newMonoUser = accountDetails.flatMap(newUser -> {
			Flowable<Integer> transactionId = DatabaseUtil.getInstance().getDatabase()
					.update(WithdrawQueryConstants.USER_WITHDRAW_CREATE)
					.parameters("W", newUser.getBalance(), "Y", new Date(), newUser.getAccountId()).counts();
			Flowable<TransactionPojo> webDepositPojo = DatabaseUtil.getInstance().getDatabase()
					.select(WithdrawQueryConstants.WITHDRAW_BALANCE_BY_ACCOUNT_NUMBER).parameterStream(transactionId)
					.get(rs -> {
						return prepareTransactionPojo(newUser, rs);
					});
			return Mono.from(webDepositPojo);
		});
		return newMonoUser;
	}

	/**
	 * @param monoUser
	 * @return
	 */

	public Mono<WebDepositPojo> accountdetails(WebDepositPojo user/* BigDecimal balance,String accountnumber */) {
		Flowable<WebDepositPojo> employeeFlowable = DatabaseUtil.getInstance().getDatabase()
				.select(WithdrawQueryConstants.GET_ACCOUNT_DETAILS).parameters(user.getAccountNum()).get(rs -> {
					WebDepositPojo accountManagement = new WebDepositPojo();
					accountManagement.setAccountId(rs.getInt(WithdrawServiceConstants.ACCOUNT_ID));
					accountManagement.setCustomerId(rs.getInt(WithdrawServiceConstants.CUSTOMER_ID));
					accountManagement.setBalance((user.getBalance()));
					accountManagement.setAccountNum(rs.getString(WithdrawServiceConstants.ACCOUNT_NUMBER));
					accountManagement.setAccountType(rs.getString(WithdrawServiceConstants.ACCOUNT_TYPE));
					accountManagement.setActiveFlag(rs.getString(WithdrawServiceConstants.ACTIVE_FLAG));
					return accountManagement;
				});
		return Mono.from(employeeFlowable);
	}

	private Mono<Account> getAccountInfo(Mono<TransactionPojo> monoUser) {
		return monoUser.flatMap(newUser -> {
			Flowable<Account> accountPojo = DatabaseUtil.getInstance().getDatabase()
					.select(WithdrawQueryConstants.ACCOUNT_BALANCE).parameter(newUser.getAccountId()).get(rs -> {
						return prepareAccountPojo(newUser, rs);
					});
			return Mono.from(accountPojo);
		});
	}

	private Mono<Status> updateAccountMono(Mono<Account> webDepositPojo) {
		return webDepositPojo.flatMap(newUser -> {
			Flowable<Integer> updatedCount = DatabaseUtil.getInstance().getDatabase()
					.select(WithdrawQueryConstants.ACCOUNT_BALANCE_BY_ID).parameter(newUser.getAccountId())
					.getAs(Integer.class)
					.flatMap(account -> DatabaseUtil.getInstance().getDatabase().update(WithdrawQueryConstants.UPDATE_ACCOUNT_BALANCE)
							.parameters(newUser.getBalance(), newUser.getAccountType(), new Date(),
									newUser.getAccountId())
							.counts());
			Flowable<Status> result = updatedCount.map(new Function<Integer, Status>() {
				@Override
				public Status apply(Integer count) throws Exception {
					Status status = null;
					if (count > 0) {
						status = util.prepareStatus("00", "Success");
					} else {
						status = util.prepareStatus("99", "Failed");
					}
					return status;
				}
			});
			return Mono.from(result);
		});
	}

	private Account prepareAccountPojo(TransactionPojo newUser, ResultSet rs) throws SQLException {
		Account accountPojo = new Account();
		accountPojo.setActiveFlag(newUser.getActiveFlag());
		accountPojo.setAccountType(newUser.getTransactionType());
		accountPojo.setAccountId(newUser.getAccountId());
		BigDecimal balance = new BigDecimal(rs.getDouble("balance")).subtract(newUser.getWithdrawAmount());
		accountPojo.setBalance(balance);
		return accountPojo;
	}

	private TransactionPojo prepareTransactionPojo(WebDepositPojo newUser, ResultSet rs) throws SQLException {
		TransactionPojo transactionPojo = new TransactionPojo();
		transactionPojo.setWithdrawAmount(newUser.getBalance());
		transactionPojo.setActiveFlag(newUser.getActiveFlag());
		transactionPojo.setTransactionType(newUser.getAccountType());
		transactionPojo.setAccountId(newUser.getAccountId());
		return transactionPojo;
	}

	private WebDepositPojo accountDetailsPojo(WebDepositPojo newUser, ResultSet rs) throws SQLException {
		WebDepositPojo webDepositPojo = new WebDepositPojo();
		webDepositPojo.setAccountId(rs.getInt(WithdrawServiceConstants.ACCOUNT_ID));
		webDepositPojo.setCustomerId(rs.getInt(WithdrawServiceConstants.CUSTOMER_ID));
		webDepositPojo.setAccountNum(newUser.getAccountNum());
		webDepositPojo.setBalance(newUser.getBalance());
		return webDepositPojo;
	}

}

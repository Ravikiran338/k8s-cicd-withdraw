/**
 * 
 */
package com.sci.services.account.constants;

/**
 * @author mn259
 *
 */
public class WithdrawQueryConstants {
	

	public static final String WITHDRAW_BALANCE_BY_ACCOUNT_NUMBER = "SELECT * FROM transaction_tbl e " + " WHERE transaction_id = ?";
	public static final String USER_WITHDRAW_CREATE = "INSERT INTO transaction_tbl (transaction_type, withdraw_amt, active_flag, transaction_datetime, account_id) VALUES (?, ?, ?,?,?)";
	public static final String GET_ACCOUNT_DETAILS = "SELECT * FROM accounts_tbl where account_num = ?";
	public static final String ACCOUNT_BALANCE = "SELECT balance FROM accounts_tbl WHERE account_id = ?";
	public static final String ACCOUNT_BALANCE_BY_ID = "SELECT * FROM accounts_tbl e " + " WHERE account_id = ?";

	public static final String UPDATE_ACCOUNT_BALANCE = "UPDATE accounts_tbl set balance=?,account_type=?,account_created_datetime=? where account_id =?";
	
	
	
	
	
}

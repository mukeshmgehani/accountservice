/**
 * 
 */
package com.mukesh;

import java.math.BigDecimal;
import java.util.UUID;

import com.mukesh.entity.Account;
import com.mukesh.entity.Transaction;
import com.mukesh.model.AccountBO;
import com.mukesh.model.Currency;
import com.mukesh.model.TransactionBO;

/**
 * @author Mukesh Gehani
 *
 */
public class TestDataPreparation {
	
	/**
	 * @return
	 */
	public static TransactionBO getTransactionBO() {
		TransactionBO transactionBO = new TransactionBO();
		transactionBO.setAmount(new BigDecimal(50.00));
		transactionBO.setSourceAccountNumber(UUID.randomUUID().toString());
		transactionBO.setTargetAccountNumber(UUID.randomUUID().toString());
		transactionBO.setCurrency(Currency.GBP);
		return transactionBO;
	}
	
	/**
	 * @return
	 */
	public static AccountBO getAccountBO() {
		AccountBO accountBO = new AccountBO();
		accountBO.setAccountNumber(UUID.randomUUID().toString());
		accountBO.setCurrency(Currency.GBP);
		accountBO.setBalance(new BigDecimal(100.00));
			return accountBO;
	}
	
	public static Transaction getTransaction() {
		Transaction transaction = new Transaction();
		transaction.setAmount(new BigDecimal(300.00));
		transaction.setCurrency(Currency.GBP);
		transaction.setTargetAccountNumber(UUID.randomUUID().toString());
		transaction.setSourceAccountNumber(UUID.randomUUID().toString());
		return transaction;
	}

	/**
	 * @return
	 */
	public static Account getAccount() {
		Account account = new Account();
		account.setAccountNumber(UUID.randomUUID().toString());
		account.setCurrency(Currency.GBP);
		account.setBalance(new BigDecimal(100.00));
		return account;
	}

}

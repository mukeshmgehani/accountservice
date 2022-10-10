package com.mukesh.service;

import java.util.List;

import com.mukesh.model.AccountBO;
import com.mukesh.model.TransactionBO;
import com.mukesh.model.TransactionResponseBO;

public interface AccountService extends BaseService {

	public AccountBO createAccount(AccountBO productBO);

	public AccountBO accountById(Long productId);

	public List<AccountBO> accounts();

	public AccountBO delete(Long productId);

	public TransactionResponseBO transferAmount(TransactionBO transactionBO);

	public AccountBO updateAccount(AccountBO accountBO, Long accountId);
	
}

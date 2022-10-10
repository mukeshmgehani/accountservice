package com.mukesh.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.mukesh.entity.Account;
import com.mukesh.model.TransactionBO;

public interface TransactionService extends BaseService {

	 TransactionBO transactionById(Long categoryId);

	 List<TransactionBO> transactions();


	CompletableFuture<TransactionBO> createTransaction(@Valid TransactionBO transactionBO, Account sourceAccount);

}

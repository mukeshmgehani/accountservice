package com.mukesh.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mukesh.common.Messages;
import com.mukesh.entity.Account;
import com.mukesh.exception.DataNotFoundException;
import com.mukesh.exception.RequestProcessException;
import com.mukesh.model.AccountBO;
import com.mukesh.model.TransactionBO;
import com.mukesh.model.TransactionResponseBO;
import com.mukesh.repository.AccountRepository;
import com.mukesh.service.AccountService;
import com.mukesh.service.TransactionService;
import com.mukesh.transformer.AccountTransformer;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	private static final Logger log = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountTransformer accountTransformer;

	@Autowired
	private TransactionService transactionService;

	@Override
	public AccountBO createAccount(AccountBO accountBO) {
		log.info("AccountServiceImpl.createAccount() has been called ");
		accountBO.setAccountNumber(UUID.randomUUID().toString());
		Account account = accountTransformer.boToEntity(accountBO);
		return accountTransformer.entityToBo(accountRepository.saveAndFlush(account));
	}

	@Override
	public AccountBO accountById(Long accountId) {
		log.info("AccountServiceImpl.accountById() has been called with account Id =", accountId);
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new DataNotFoundException(Messages.ACCOUNT_ID_NOT_FOUND + accountId));
		;
		return accountTransformer.entityToBo(account);
	}

	@Override
	public List<AccountBO> accounts() {
		log.info("AccountServiceImpl.accounts() has been called ");
		return accountRepository.findAll().stream() //
				.map(accountTransformer::entityToBo) //
				.collect(Collectors.toList());
	}

	@Override
	public AccountBO delete(Long accountId) {
		log.info("AccountServiceImpl.delete() has been called with ad Id =", accountId);
		AccountBO accountBO = accountById(accountId);
		accountRepository.deleteById(accountId);
		return accountBO;
	}

	@Override
	public TransactionResponseBO transferAmount(@Valid TransactionBO transactionBO) {
		log.info("AccountServiceImpl.transferAmount() has been called ");

		Account sourceAccount = sourceAccountCheck(transactionBO);
		Account targetAccount = targetAccountCheck(transactionBO);

		sourceAccount.setBalance(sourceAccount.getBalance().subtract(transactionBO.getAmount()));
		targetAccount.setBalance(targetAccount.getBalance().add(transactionBO.getAmount()));

		CompletableFuture<TransactionBO> transactionBOWithSourceAccount = transactionService
				.createTransaction(transactionBO, sourceAccount);
		CompletableFuture<TransactionBO> transactionBOWithTargetAccount = transactionService
				.createTransaction(transactionBO, targetAccount);
		TransactionResponseBO transactionResponseBO = new TransactionResponseBO();
		if (transactionBOWithSourceAccount.isDone() && transactionBOWithTargetAccount.isDone()) {
			try {
				List<TransactionBO> transactionBOs = new ArrayList<>();
				transactionBOs.add(transactionBOWithSourceAccount.get());
				transactionBOs.add(transactionBOWithTargetAccount.get());
				transactionResponseBO.setTransactionBOs(transactionBOs);
				transactionResponseBO.setMessage(Messages.TRANSACTION_COMPLETED);
				return transactionResponseBO;
			} catch (InterruptedException | ExecutionException e) {
				log.error("AccountServiceImpl.transferAmount() transaction failed ");
				throw new RequestProcessException(Messages.TRANSACTION_FAILED + e.getMessage());
			}
		}
		transactionResponseBO.setMessage(Messages.TRANSACTION_IN_PROCESSING);
		return transactionResponseBO;
	}

	private Account sourceAccountCheck(TransactionBO transactionBO) {
		log.debug("AccountServiceImpl.sourceAccountCheck() has been called ");
		if (transactionBO.getSourceAccountNumber().equals(transactionBO.getTargetAccountNumber())) {
			throw new RequestProcessException(Messages.TRANSACTION_FAILED_ACCOUNT_ARE_SAME
					.formatted(transactionBO.getSourceAccountNumber(), transactionBO.getTargetAccountNumber()));
		}
			
		Optional<Account> sourceAccountOptional = accountRepository
				.findByAccountNumber(transactionBO.getSourceAccountNumber());

		accountExistsCheck(transactionBO.getSourceAccountNumber(), sourceAccountOptional);

		if (sourceAccountOptional.get().getBalance().compareTo(transactionBO.getAmount()) < 0) {
			throw new RequestProcessException(
					Messages.TRANSACTION_FAILED_INSUFFICIENT_BALANCE.formatted(sourceAccountOptional.get().getBalance()));
		}
		return sourceAccountOptional.get();

	}

	private Account targetAccountCheck(TransactionBO transactionBO) {
		log.debug("AccountServiceImpl.targetAccountCheck() has been called ");
		Optional<Account> targetAccountOptional = accountRepository
				.findByAccountNumber(transactionBO.getTargetAccountNumber());
		accountExistsCheck(transactionBO.getTargetAccountNumber(), targetAccountOptional);
		return targetAccountOptional.get();

	}

	/**
	 * @param transactionBO
	 * @param account
	 */
	private void accountExistsCheck(String accountNumber, Optional<Account> account) {
		log.debug("AccountServiceImpl.accountExistsCheck() has been called ");
		if (account.isEmpty()) {
			throw new RequestProcessException(Messages.TRANSACTION_FAILED_ACCOUNT_NOT_FOUND + accountNumber);

		}
	}

	@Override
	public AccountBO updateAccount(AccountBO accountBO, Long accountId) {
		log.info("AccountServiceImpl.updateAccount() has been called account id --" + accountId);
		AccountBO accountBoUpdate = accountById(accountId);
		accountBO = accountTransformer.boToBo(accountBO, accountBoUpdate);
		Account account = accountTransformer.boToEntity(accountBO);
		return accountTransformer.entityToBo(accountRepository.saveAndFlush(account));
	}

}

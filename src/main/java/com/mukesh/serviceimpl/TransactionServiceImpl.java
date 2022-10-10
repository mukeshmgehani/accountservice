package com.mukesh.serviceimpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mukesh.common.Messages;
import com.mukesh.entity.Account;
import com.mukesh.entity.Transaction;
import com.mukesh.exception.DataNotFoundException;
import com.mukesh.model.TransactionBO;
import com.mukesh.repository.TransactionRepository;
import com.mukesh.service.TransactionService;
import com.mukesh.transformer.TransactionTransformer;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private static final Logger log = LogManager.getLogger(TransactionServiceImpl.class);
	
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransactionTransformer transactionTransformer;
	
	
	@Override
	public TransactionBO transactionById(Long transactionId) {
		log.info("TransactionServiceImpl.transactionById() has been called with category Id =", transactionId);
		Transaction category = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new DataNotFoundException(Messages.TRANSACTION_ID_NOT_FOUND + transactionId));;
		return transactionTransformer.entityToBo(category);
	}

	@Override
	public List<TransactionBO> transactions() {
		log.info("TransactionServiceImpl.transactions() has been called ");
		List<Transaction> categories = transactionRepository.findAll();
		return categories.stream().map(transactionTransformer::entityToBo).collect(Collectors.toList());
	}

	@Override
	 @Async("threadPoolTaskExecutor")
	public CompletableFuture<TransactionBO> createTransaction(@Valid TransactionBO transactionBO, Account account) {
	log.info("TransactionServiceImpl.createTransaction() has been called ");
		
		Transaction transaction = transactionTransformer.boToEntity(transactionBO);
		transaction.setAccount(account);
		
		return CompletableFuture.completedFuture(transactionTransformer.entityToBo(transactionRepository.saveAndFlush(transaction)));
		
	
	}

}

package com.mukesh.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mukesh.model.AccountBO;
import com.mukesh.model.TransactionBO;
import com.mukesh.model.TransactionResponseBO;
import com.mukesh.service.AccountService;
import com.mukesh.service.BaseService;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController implements BaseService {

	private static final Logger log = LogManager.getLogger(AccountController.class);
	
	
	@Autowired
	private  AccountService accountService;


	@GetMapping(value = "/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AccountBO> accountById(@PathVariable("id") Long accountId) {
		log.info("AccountController.accountById() has been called with account Id =", accountId);
		return new ResponseEntity<AccountBO>(accountService.accountById(accountId), accountService.prepareHeaders(), HttpStatus.OK);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<AccountBO>> accounts() {
		log.info("AccountController.accounts() has been called ");
		return new ResponseEntity<List<AccountBO>>(accountService.accounts(), accountService.prepareHeaders(), HttpStatus.OK);
	}

	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE } )
	public ResponseEntity<AccountBO> createAccount(@Valid @RequestBody AccountBO accountBO) {
		log.info("AccountController.create() has been called ");
		return new ResponseEntity<AccountBO>(accountService.createAccount(accountBO), accountService.prepareHeaders(), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}",produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE } )
	public ResponseEntity<AccountBO> updateAccount(@PathVariable("id") Long accountId, @RequestBody AccountBO accountBO) {
		log.info("AccountController.updateAccount() has been called with account Id = "+accountId);
		return new ResponseEntity<AccountBO>(accountService.updateAccount(accountBO,accountId), accountService.prepareHeaders(), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AccountBO> delete(@PathVariable("id") Long accountId) {
		log.info("AccountController.delete() has been called with account Id =", accountId);
		return new ResponseEntity<AccountBO>(accountService.delete(accountId), accountService.prepareHeaders(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/fundTransfer",produces = { MediaType.APPLICATION_JSON_VALUE },consumes = { MediaType.APPLICATION_JSON_VALUE } )
	public ResponseEntity<TransactionResponseBO> transferAmount(@Valid @RequestBody TransactionBO transactionBO) {
		log.info("AccountController.transferAmount() has been called ");
		return new ResponseEntity<TransactionResponseBO>(accountService.transferAmount(transactionBO), accountService.prepareHeaders(), HttpStatus.ACCEPTED);
	}
}

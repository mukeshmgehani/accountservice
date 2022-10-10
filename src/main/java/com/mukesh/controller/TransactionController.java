package com.mukesh.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mukesh.model.TransactionBO;
import com.mukesh.service.TransactionService;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TransactionController {

	private static final Logger log = LogManager.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@GetMapping(value = "{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TransactionBO> transactionById(@PathVariable("id") Long transactionId) {
		log.info("TransactionController.transactionById() has been called with transaction Id =", transactionId);
		return new ResponseEntity<TransactionBO>(transactionService.transactionById(transactionId),
				transactionService.prepareHeaders(), HttpStatus.OK);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TransactionBO>> transactions() {
		log.info("TransactionController.transactions() has been called ");
		return new ResponseEntity<List<TransactionBO>>(transactionService.transactions(),
				transactionService.prepareHeaders(), HttpStatus.OK);

	}

}

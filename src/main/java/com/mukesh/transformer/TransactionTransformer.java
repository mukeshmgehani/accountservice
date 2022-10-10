package com.mukesh.transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mukesh.entity.Transaction;
import com.mukesh.model.TransactionBO;

@Component
public class TransactionTransformer {

	private static final Logger log = LogManager.getLogger(TransactionTransformer.class);
	
	@Autowired
	private ModelMapper mapper;

	public TransactionBO entityToBo(Transaction transaction) {
		log.debug("TransactionTransformer.entityToBo() has been called ");
		return mapper.map(transaction, TransactionBO.class);
	}

	public Transaction boToEntity(TransactionBO transactionBO) {
		log.debug("TransactionTransformer.boToEntity() has been called");
		return mapper.map(transactionBO, Transaction.class);
	}

}

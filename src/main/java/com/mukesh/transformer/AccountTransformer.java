package com.mukesh.transformer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mukesh.entity.Account;
import com.mukesh.model.AccountBO;

@Component
public class AccountTransformer {

	private static final Logger log = LogManager.getLogger(AccountTransformer.class);

	@Autowired
	private ModelMapper mapper;

	public AccountBO entityToBo(Account account) {
		log.debug("AccountTransformer.boToEntity() has been called");
		return mapper.map(account, AccountBO.class);
	}

	public Account boToEntity(AccountBO accountBO) {
		log.debug("AccountTransformer.boToEntity() has been called with accountBO data = %s"
				.formatted(accountBO.toString()));
		return mapper.map(accountBO, Account.class);
	}
	
	public AccountBO boToBo(AccountBO accountBO,AccountBO accountBOUpdate) {
		log.debug("AccountTransformer.boToBo() has been called");
		mapper.map(accountBO, accountBOUpdate);
		return accountBOUpdate;
	}

}

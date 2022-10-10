/**
 * 
 */
package com.mukesh.serviceimpl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.mukesh.TestDataPreparation;
import com.mukesh.common.Messages;
import com.mukesh.entity.Account;
import com.mukesh.exception.RequestProcessException;
import com.mukesh.model.AccountBO;
import com.mukesh.model.Currency;
import com.mukesh.model.TransactionBO;
import com.mukesh.model.TransactionResponseBO;
import com.mukesh.repository.AccountRepository;
import com.mukesh.service.AccountService;
import com.mukesh.service.TransactionService;
import com.mukesh.transformer.AccountTransformer;

/**
 * @author Mukesh Gehani
 *
 */
@SpringBootTest
class AccountServiceImplTest {

	@MockBean
	private AccountService accountService;

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountTransformer accountTransformer;

	@Mock
	private TransactionService transactionService;

	private TransactionBO transactionBO;

	private Account account;

	private AccountBO accountBO;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		transactionBO = TestDataPreparation.getTransactionBO();
		account = TestDataPreparation.getAccount();
		accountBO = TestDataPreparation.getAccountBO();
	}

	/**
	 * Test method for
	 * {@link com.mukesh.serviceimpl.AccountServiceImpl#createAccount(com.mukesh.model.AccountBO)}.
	 */
	@Test
	void testCreateAccount() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.mukesh.serviceimpl.AccountServiceImpl#accountById(java.lang.Long)}.
	 */
	@Test
	void testAccountById() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.mukesh.serviceimpl.AccountServiceImpl#accounts()}.
	 */
	@Test
	void testAccounts() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.mukesh.serviceimpl.AccountServiceImpl#delete(java.lang.Long)}.
	 */
	@Test
	void testDelete() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.mukesh.serviceimpl.AccountServiceImpl#transferAmount(com.mukesh.model.TransactionBO)}.
	 */
	@Test
	void testTransferAmountWhenSourceandTargetAccountNumberAreSame() {
		transactionBO.setTargetAccountNumber(transactionBO.getSourceAccountNumber());

		RequestProcessException requestProcessException = assertThrows(RequestProcessException.class, () -> {
			accountServiceImpl.transferAmount(transactionBO);
		});
		
		assertTrue(requestProcessException.getMessage().contains(Messages.TRANSACTION_FAILED_ACCOUNT_ARE_SAME
				.formatted(transactionBO.getSourceAccountNumber(), transactionBO.getTargetAccountNumber())));
	}

	@Test
	void testTransferAmountWhenSourceAccountNumberNotExist() {
		
		RequestProcessException requestProcessException = assertThrows(RequestProcessException.class, () -> {
			accountServiceImpl.transferAmount(transactionBO);
		});
		
		assertTrue(requestProcessException.getMessage().contains(Messages.TRANSACTION_FAILED_ACCOUNT_NOT_FOUND + transactionBO.getSourceAccountNumber()));
	}
	
	
	
	@Test
	void testTransferAmountWhenTargetAccountNumberNotExist() {
		
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getSourceAccountNumber())).thenReturn(Optional.of(account));
		
		RequestProcessException requestProcessException = assertThrows(RequestProcessException.class, () -> {
			accountServiceImpl.transferAmount(transactionBO);
		});
		
		assertTrue(requestProcessException.getMessage().contains(Messages.TRANSACTION_FAILED_ACCOUNT_NOT_FOUND + transactionBO.getTargetAccountNumber()));
	}
	
	
	@Test
	void testTransferAmountWhenSourceAccountInsufficientBalance() {
		transactionBO.setAmount(new BigDecimal(5000.00));
		
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getSourceAccountNumber())).thenReturn(Optional.of(account));
		
		RequestProcessException requestProcessException = assertThrows(RequestProcessException.class, () -> {
			accountServiceImpl.transferAmount(transactionBO);
		});
		
		assertTrue(requestProcessException.getMessage().contains(
				Messages.TRANSACTION_FAILED_INSUFFICIENT_BALANCE.formatted(account.getBalance())));
	}

	@Test
	void testTransferAmountWhenSuccess() {
		
		Account targetAccount=TestDataPreparation.getAccount();
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getSourceAccountNumber())).thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getTargetAccountNumber())).thenReturn(Optional.of(targetAccount));
		Mockito.when(transactionService.createTransaction(transactionBO, account)).thenReturn(CompletableFuture.completedFuture(transactionBO));
		Mockito.when(transactionService.createTransaction(transactionBO, targetAccount)).thenReturn(CompletableFuture.completedFuture(transactionBO));
		TransactionResponseBO transactionResponseBO=	accountServiceImpl.transferAmount(transactionBO);
		Assertions.assertEquals(Messages.TRANSACTION_COMPLETED, transactionResponseBO.getMessage());
		assertTrue(transactionResponseBO.getMessage().contains(
				Messages.TRANSACTION_COMPLETED));
	}
	
	@Test
	void testTransferAmountWhenTransactionInProcessing() {
		
		Account targetAccount=TestDataPreparation.getAccount();
		CompletableFuture<TransactionBO> transactionBOCompletableFuture= Mockito.mock(CompletableFuture.class);
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getSourceAccountNumber())).thenReturn(Optional.of(account));
		Mockito.when(accountRepository.findByAccountNumber(transactionBO.getTargetAccountNumber())).thenReturn(Optional.of(targetAccount));
		Mockito.when(transactionService.createTransaction(transactionBO, account)).thenReturn(transactionBOCompletableFuture);
		Mockito.when(transactionService.createTransaction(transactionBO, targetAccount)).thenReturn(transactionBOCompletableFuture);
		Mockito.when(transactionBOCompletableFuture.isDone()).thenReturn(false);
		TransactionResponseBO transactionResponseBO=	accountServiceImpl.transferAmount(transactionBO);
		Assertions.assertEquals(Messages.TRANSACTION_IN_PROCESSING, transactionResponseBO.getMessage());
		assertTrue(transactionResponseBO.getMessage().contains(
				Messages.TRANSACTION_IN_PROCESSING));
	}

	
	/**
	 * Test method for
	 * {@link com.mukesh.serviceimpl.AccountServiceImpl#updateAccount(com.mukesh.model.AccountBO, java.lang.Long)}.
	 */
	@Test
	void testUpdateAccount() {
//		fail("Not yet implemented");
	}

	/**
	 * @return
	 */
	private TransactionBO getTransactionBO() {
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
	private AccountBO getAccountBO() {
		AccountBO accountBO = new AccountBO();
		accountBO.setAccountNumber(UUID.randomUUID().toString());
		accountBO.setCurrency(Currency.GBP);
		accountBO.setBalance(new BigDecimal(100.00));
		return accountBO;
	}
}

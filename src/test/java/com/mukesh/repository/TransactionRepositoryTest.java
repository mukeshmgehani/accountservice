/**
 * 
 */
package com.mukesh.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mukesh.TestDataPreparation;
import com.mukesh.entity.Account;
import com.mukesh.entity.Transaction;

/**
 * @author Mukesh
 *
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.mukesh.Application.class)
@TestPropertySource(value = "classpath:application-test.properties")
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	public void setUp() throws Exception {
		transactionRepository.deleteAll();
		accountRepository.deleteAll();
	}

	@Test
	public void transactionRepositoryTest() {
		Account account =  TestDataPreparation.getAccount();
		accountRepository.save(account);

		Transaction transaction =  TestDataPreparation.getTransaction();

		transaction.setSourceAccountNumber(account.getAccountNumber());
		transaction.setAccount(account);
		transaction = transactionRepository.saveAndFlush(transaction);

		List<Transaction> categories = (List<Transaction>) transactionRepository.findAll();
		assertFalse(categories.isEmpty());

		BigDecimal amount = new BigDecimal(100.00);
		Transaction transaction2 = transactionRepository.findById(transaction.getId()).get();
		transaction2.setAmount(amount);
		transactionRepository.saveAndFlush(transaction2);

		transaction2 = transactionRepository.findById(transaction2.getId()).get();
		assertEquals(transaction2.getAmount(), amount.setScale(2));

		transactionRepository.deleteById(transaction.getId());

		transaction = transactionRepository.findById(transaction.getId()).orElse(null);
		assertNull(transaction);
	}

	@Test
	public void shouldNotAllowToPersistNullAccountNumber() {
		Transaction transaction = TestDataPreparation.getTransaction();
		transaction.setSourceAccountNumber(null);
		assertThrows(ConstraintViolationException.class, () -> transactionRepository.saveAndFlush(transaction));
	}


}

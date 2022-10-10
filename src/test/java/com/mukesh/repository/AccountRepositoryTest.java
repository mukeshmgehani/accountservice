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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.mukesh.TestDataPreparation;
import com.mukesh.entity.Account;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.mukesh.Application.class)
@TestPropertySource(value = "classpath:application-test.properties")
public class AccountRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	public void setup() throws Exception {
		MockitoAnnotations.openMocks(this);
		accountRepository.deleteAll();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void accountRepositoryTest() {
		Account account = TestDataPreparation.getAccount();

		accountRepository.save(account);
		List<Account> accounts = (List<Account>) accountRepository.findAll();
		assertFalse(accounts.isEmpty());
		BigDecimal balance=new BigDecimal("200.50");
	
		Account account2 = accountRepository.findById(account.getId()).get();
		account2.setBalance(balance);
		
		accountRepository.saveAndFlush(account2);

		account = accountRepository.findById(account2.getId()).get();

		assertEquals(account.getBalance(), balance);

		accountRepository.deleteAll();
		accountRepository.findAll().forEach(System.out::println);
		account = accountRepository.findById(account.getId()).orElse(null);
		assertNull(account);
	}

	@Test
	public void shouldNotAllowToPersistNullAccountNumber() {
		Account account = TestDataPreparation.getAccount();
		account.setAccountNumber(null);
		assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(account));
	}

	

}

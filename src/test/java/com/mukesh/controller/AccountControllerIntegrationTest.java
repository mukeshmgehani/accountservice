/**
 * 
 */
package com.mukesh.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.mukesh.entity.Account;
import com.mukesh.repository.AccountRepository;
import com.mukesh.repository.TransactionRepository;
import com.mukesh.service.AccountService;
import com.mukesh.service.TransactionService;

/**
 * @author Mukesh
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerIntegrationTest {

	@MockBean
	private AccountService accountService;

	@MockBean
	private TransactionService transactionService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountController accountController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void whenAccountControllerInjected_thenNotNull() throws Exception {
		assertThat(accountController).isNotNull();
	}

	@Test
	public void whenGetRequestToAccounts_thenCorrectResponse() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/accounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

	}

	@Test
	public void whenPostRequestToAccountsAndValidAccount_thenCorrectResponse() throws Exception {
		String account = "{\"currency\": \"GBP\",\"balance\": 200.00}";
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts").content(account).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void whenPostRequestToAccountsAndInvalidAccountName_thenValidationFailedReponse() throws Exception {

		String account = "{\"balance\": 200.00}";
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts").content(account).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.message", Is.is("Validation Failed"))).andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasItem("Currency cannot be null . It should contains value from this -- (GBP, EUR, INR, AUS, USD )")));

		verify(accountRepository, times(0)).save(Mockito.any(Account.class));
	}

	@Test
	public void whenGetRequestToAccountBasedOnValidId_thenCorrectResponse() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}

	@Test
	public void whenDeleteRequestToAccountBasedOnValidId_thenCorrectResponse() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	@Test
	public void whenPostRequestToAccountsFundTransferAndInvalidSourceAccountNumber_thenValidationFailedReponse() throws Exception {

		String transaction = " {\"targetAccountNumber\": \"3f263f59-4752-4ec7-80c9-36c37995f816\",\"currency\": \"GBP\",\"amount\": 50.00}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/fundTransfer").content(transaction).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.message", Is.is("Validation Failed"))).andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasItem("Source Account Number is not provided..!!")));

		verify(accountRepository, times(0)).save(Mockito.any(Account.class));
	}
	
	@Test
	public void whenPostRequestToAccountsFundTransferAndInvalidTargetAccountNumber_thenValidationFailedReponse() throws Exception {

		String transaction = " {\"sourceAccountNumber\": \"3f263f59-4752-4ec7-80c9-36c37995f816\",\"currency\": \"GBP\",\"amount\": 50.00}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/fundTransfer").content(transaction).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.message", Is.is("Validation Failed"))).andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasItem("Target Account Number is not provided..!!")));

		verify(accountRepository, times(0)).save(Mockito.any(Account.class));
	}
	
	@Test
	public void whenPostRequestToAccountsFundTransferAndInvalidCurrency_thenValidationFailedReponse() throws Exception {

		String transaction = " { \"sourceAccountNumber\": \"93b79331-0126-468d-adb7-928206c9d06f\",\"targetAccountNumber\": \"3f263f59-4752-4ec7-80c9-36c37995f816\",\"amount\": 50.00}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/fundTransfer").content(transaction).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.message", Is.is("Validation Failed"))).andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasItem("Currency cannot be null . It should contains value from this -- (GBP, EUR, INR, AUS, USD )")));

		verify(accountRepository, times(0)).save(Mockito.any(Account.class));
	}
	
	@Test
	public void whenPostRequestToAccountsFundTransferAndInvalidAmount_thenValidationFailedReponse() throws Exception {

		String transaction = " { \"sourceAccountNumber\": \"93b79331-0126-468d-adb7-928206c9d06f\",\"targetAccountNumber\": \"3f263f59-4752-4ec7-80c9-36c37995f816\",\"currency\": \"GBP\", \"amount\": -50.00}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/fundTransfer").content(transaction).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", is(notNullValue())))
				.andExpect(jsonPath("$.message", Is.is("Validation Failed"))).andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasItem("Amount should be greater than 0.0")));

		verify(accountRepository, times(0)).save(Mockito.any(Account.class));
	}
	
	
	@Test
	public void whenPostRequestToAccountsFundTransfer() throws Exception {

		String transaction = " { \"sourceAccountNumber\": \"93b79331-0126-468d-adb7-928206c9d06f\",\"targetAccountNumber\": \"3f263f59-4752-4ec7-80c9-36c37995f816\",\"currency\": \"GBP\", \"amount\": 50.00}";
		
		mockMvc.perform(MockMvcRequestBuilders.post("/accounts/fundTransfer").content(transaction).contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isAccepted());
	}

}

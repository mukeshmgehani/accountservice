/**
 * 
 */
package com.mukesh.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mukesh.repository.AccountRepository;
import com.mukesh.repository.TransactionRepository;
import com.mukesh.service.AccountService;
import com.mukesh.service.TransactionService;

/**
 * @author Mukesh
 *
 */
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerIntegrationTest {

	@MockBean
	private AccountService accountService;

	@MockBean
	private TransactionService transactionService;

	@MockBean
	private AccountRepository accountRepository;

	@MockBean
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionController transactionController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
	}

	@Test
	public void whenCustomerControllerInjected_thenNotNull() throws Exception {
		assertThat(transactionController).isNotNull();
	}

	@Test
	public void whenGetRequestToTransaction_thenCorrectResponse() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transactions").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

	}

	
	@Test
	public void whenGetRequestToTransactionBasedOnValidId_thenCorrectResponse() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/transactions/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

}

package com.mukesh.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mukesh.TestDataPreparation;

/**
 * @author Mukesh
 *
 */
public class TransactionBOTest {
	private Validator validator;

	@BeforeEach
	public void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void whenCurrencyIsNull_thenValidationFails() {
		TransactionBO transactionBO = TestDataPreparation.getTransactionBO();
		transactionBO.setCurrency(null);

		Set<ConstraintViolation<TransactionBO>> violations = validator.validate(transactionBO);
		assertEquals(1, violations.size());
		assertEquals("Currency cannot be null . It should contains value from this -- (GBP, EUR, INR, AUS, USD )",
				violations.iterator().next().getMessage());
	}

	@Test
	public void whenAmountIsNegative_thenValidationFails() {
		TransactionBO transactionBO = TestDataPreparation.getTransactionBO();
		transactionBO.setAmount(new BigDecimal(0.0));

		Set<ConstraintViolation<TransactionBO>> violations = validator.validate(transactionBO);
		assertEquals(1, violations.size());
		assertEquals("Amount should be greater than 0.0", violations.iterator().next().getMessage());
	}
	

	
	@Test
	public void whenTargetAccountIsNotProvided_thenValidationFails() {
		TransactionBO transactionBO = TestDataPreparation.getTransactionBO();
		transactionBO.setTargetAccountNumber(null);

		Set<ConstraintViolation<TransactionBO>> violations = validator.validate(transactionBO);
		assertEquals(1, violations.size());
		assertEquals("Target Account Number is not provided..!!", violations.iterator().next().getMessage());
	}

	@Test
	public void whenSourceAccountIsNotProvided_thenValidationFails() {
		TransactionBO transactionBO =TestDataPreparation.getTransactionBO();
		transactionBO.setSourceAccountNumber(null);

		Set<ConstraintViolation<TransactionBO>> violations = validator.validate(transactionBO);
		assertEquals("Source Account Number is not provided..!!", violations.iterator().next().getMessage());
	}
	
}

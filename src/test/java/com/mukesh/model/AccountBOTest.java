/**
 * 
 */
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
 * @author Mukesh Gehani
 *
 */
public class AccountBOTest {

	private Validator validator;

	@BeforeEach
	public void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void whenCurrencyIsNull_thenValidationFails() {
		AccountBO accountBO = TestDataPreparation.getAccountBO();
		accountBO.setCurrency(null);

		Set<ConstraintViolation<AccountBO>> violations = validator.validate(accountBO);
		assertEquals(1, violations.size());
		assertEquals("Currency cannot be null . It should contains value from this -- (GBP, EUR, INR, AUS, USD )",
				violations.iterator().next().getMessage());
	}


	@Test
	public void whenBalanceIsNegative_thenValidationFails() {
		AccountBO accountBO  = TestDataPreparation.getAccountBO();
		accountBO.setBalance(new BigDecimal(0));

		Set<ConstraintViolation<AccountBO>> violations = validator.validate(accountBO);
		assertEquals(1, violations.size());
		assertEquals("Balance should be greater than 0.0", violations.iterator().next().getMessage());
	}

}

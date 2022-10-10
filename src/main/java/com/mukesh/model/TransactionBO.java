package com.mukesh.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_EMPTY)
public class TransactionBO extends BaseModel {

	private Long id;

	@NotNull(message = "Source Account Number is not provided..!!")
	private String SourceAccountNumber;

	@NotNull(message = "Target Account Number is not provided..!!")
	private String targetAccountNumber;

	@NotNull(message = "Currency cannot be null . It should contains value from this -- (GBP, EUR, INR, AUS, USD )")
	private Currency currency;

	@Positive(message = "Amount should be greater than 0.0")
	private BigDecimal amount;
	
	private AccountBO accountBO;

}

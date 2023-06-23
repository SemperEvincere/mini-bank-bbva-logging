package com.bbva.minibank.application.usecases.account;

import java.math.BigDecimal;

public interface IAccountOperationsUseCase {
	
	BigDecimal substract(BigDecimal balance,
	                     BigDecimal amount);
	
	BigDecimal add(BigDecimal balance,
	               BigDecimal amount);
}

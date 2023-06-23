package com.bbva.minibank.presentation.request.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransactionCreateRequest {
	
	@NotNull(message = "El campo type no puede ser null")
	@Pattern(regexp = "^(DEPOSIT|WITHDRAW|TRANSFER)$", message = "El campo type debe ser DEPOSIT, WITHDRAW o TRANSFER")
	private String type;
	
	@NotNull(message = "El campo idClient no puede ser null")
	@Pattern(regexp = "^\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}$", message = "El campo idClient debe tener el formato de un UUID válido")
	private String idClient;
	
	@Pattern(regexp = "^\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}$", message = "El campo idAccountDestination debe tener el formato de un UUID válido")
	@NotNull(message = "El campo idAccountDestination no puede ser null")
	private String idAccountOrigin;
	
	@Pattern(regexp = "^(?:\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12})?$", message = "El campo idAccountOrigin debe tener el formato de un UUID válido o ser vacío")
	private String idAccountDestination;
	
	@NotNull(message = "El campo amount no puede ser null")
	@PositiveOrZero(message = "El campo amount debe ser mayor o igual a 0")
	private BigDecimal amount;
	
}

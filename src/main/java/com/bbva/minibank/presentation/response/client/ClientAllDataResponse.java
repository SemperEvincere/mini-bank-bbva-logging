package com.bbva.minibank.presentation.response.client;

import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JsonInclude()
@Builder
@Getter
@Setter
public class ClientAllDataResponse {
	
	private UUID id;
	private LocalDate createDate;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private List<AccountDetailsResponse> accounts;
	private LocalDate updatedAt;
	
	
}

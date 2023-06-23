package com.bbva.minibank.presentation.mappers;

import com.bbva.minibank.application.usecases.client.IClientFindByUseCase;
import com.bbva.minibank.domain.models.Account;
import com.bbva.minibank.presentation.response.account.AccountCreateResponse;
import com.bbva.minibank.presentation.response.account.AccountDetailsResponse;
import com.bbva.minibank.presentation.response.account.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Log4j2
public class AccountPresentationMapper {
	
	private final IClientFindByUseCase clientFindByUseCase;
	
	public List<AccountResponse> domainToResponseList(List<Account> accounts) {
		Set<AccountResponse> accountResponses = new HashSet<>();
		for (Account account : accounts) {
			accountResponses.add(domainToResponse(account));
		}
		log.info("Account responses: {}", accountResponses);
		return accountResponses.stream()
		                       .toList();
	}
	
	private AccountResponse domainToResponse(Account account) {
		log.info("Account: {}", account);
		return AccountResponse.builder()
		                      .id(account.getAccountNumber())
		                      .balance(account.getBalance())
		                      .currency(account.getCurrency())
		                      .build();
	}
	
	public AccountCreateResponse domainToCreateResponse(Account account) {
		log.info("Account: {}", account);
		return AccountCreateResponse.builder()
		                            .accountId(account.getAccountNumber())
		                            .holderId(account.getClientHolder())
		                            .secondHolderId(account.getListSecondsHolders())
		                            .balance(account.getBalance())
		                            .currency(account.getCurrency())
		                            .build();
	}
	
	public AccountDetailsResponse domainToDetailsResponse(Account account) {
		UUID holderId = account.getClientHolder();
		String holderName = clientFindByUseCase.findById(holderId)
		                                       .orElseThrow()
		                                       .getFullName();
		
		Map<UUID, String> secondsHolderMap = new HashMap<>();
		List<UUID> secondsHolderIds = account.getListSecondsHolders();
		assert secondsHolderIds != null;
		for (UUID secondsHolderId : secondsHolderIds) {
			if (secondsHolderId.compareTo(holderId) == 0)
				continue;// Si el titular es el mismo que el segundo titular, no lo añadimos (ya lo hemos añadido antes)
			String secondsHolderName = clientFindByUseCase.findById(secondsHolderId)
			                                              .orElseThrow()
			                                              .getFullName();
			log.info("Seconds holder name: {}", secondsHolderName);
			secondsHolderMap.put(secondsHolderId, secondsHolderName);
		}
		return AccountDetailsResponse.builder()
		                             .accountNumber(account.getAccountNumber())
		                             .creationDate(account.getCreationDate())
		                             .holderId(holderId)
		                             .holderName(holderName)
		                             .mapSecondsHolderId(secondsHolderMap)
		                             .currency(account.getCurrency())
		                             .balance(account.getBalance())
		                             .isLocked(account.isLocked())
		                             .build();
	}
	
	public List<AccountDetailsResponse> domainToDetailResponseList(List<Account> accounts) {
		Set<AccountDetailsResponse> accountDetailsResponses = new HashSet<>();
		for (Account account : accounts) {
			accountDetailsResponses.add(domainToDetailsResponse(account));
		}
		log.info("Account details responses: {}", accountDetailsResponses);
		return accountDetailsResponses.stream()
		                              .toList();
	}
}

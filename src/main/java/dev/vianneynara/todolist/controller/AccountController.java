package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.dto.AccountDto;
import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.exceptions.AccountExistsException;
import dev.vianneynara.todolist.exceptions.UnauthorizedException;
import dev.vianneynara.todolist.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Speaking method, used to test Spring Boot application context
	 */
	public String speak() {
		return "I am ".concat(this.getClass().getName()) + "!";
	}

	/**
	 * Gather all account in the database.
	 *
	 * @return all account.
	 */
	@GetMapping("/all-accounts")
	public ResponseEntity<Object> getAccounts(
		@RequestHeader(name = "System-PIN") String systemPin
	) {
		if (!"todolist".equals(systemPin)) {
			throw new UnauthorizedException("Invalid system PIN");
		}

		List<Account> accounts = (List<Account>) accountService.findAll();
		List<AccountDto> accountDtos = accounts.stream()
			.map(this::convertToDto)
			.collect(Collectors.toList());

		return ResponseEntity.ok(accountDtos);
	}

	/**
	 * POST routing that creates a new account.
	 *
	 * @param requestBody a JSON of `username` and `password`.
	 * @return `OK` status for newly created resource, `CONFLICT` status if username already exists.
	 */
	@PostMapping("/accounts")
	public ResponseEntity<AccountDto> createAccount(
		@RequestBody Map<String, String> requestBody
	) {
		String username = requestBody.get("username");
		String password = requestBody.get("password");

		if (accountService.accountExists(username)) {
			throw new AccountExistsException("Account with username " + username + " already exists");
		}

		Account account = new Account();
		account.setUsername(username);
		account.setPassword(password);

		Account savedAccount = accountService.save(account);

		AccountDto accountDto = convertToDto(savedAccount);

		return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
	}

	private AccountDto convertToDto(Account account) {
		return new AccountDto(
			account.getAccountId(),
			account.getUsername(),
			account.getTasks().size(),
			account.getCreatedAt(),
			account.getUpdatedAt()
		);
	}
}

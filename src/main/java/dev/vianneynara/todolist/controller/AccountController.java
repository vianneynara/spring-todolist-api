package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.exceptions.AccountExistsException;
import dev.vianneynara.todolist.exceptions.UnauthorizedException;
import dev.vianneynara.todolist.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/** Speaking method, used to test Spring Boot application context */
	public String speak() {
		return "I am ".concat(this.getClass().getName()) + "!";
	}

	/**
	 * Gather all account in the database.
	 * @return all account.
	 */
	@GetMapping("/all-accounts")
	public ResponseEntity<Object> getUsers(
		@RequestHeader(name = "System-PIN") String h_systemPin
	) {
		final String systemPin = "todolist";
		if (!(systemPin.equals(h_systemPin)))
			throw new UnauthorizedException();

		List<Account> accounts = (List<Account>) accountService.findAll();
		Set<Map<String, Object>> mappedAccounts = new HashSet<>();
		for (Account account : accounts) {
		   Map<String, Object> map = new LinkedHashMap<>();
		   map.put("userId", account.getAccountId());
		   map.put("username", account.getUsername());
		   map.put("password", account.getPassword());
		   map.put("tasks", account.getTasks().size());
		   map.put("createdAt", account.getCreatedAt());
		   map.put("updatedAt", account.getUpdatedAt());
		   mappedAccounts.add(map);
		}
		return ResponseEntity.ok(mappedAccounts);
	}

	/**
	 * POST routing that creates a new account.
	 * @param requestBody a JSON of `username` and `password`.
	 * @return `OK` status for newly created resource, `CONFLICT` status if username already exists.
	 */
	@PostMapping("/accounts")
	public ResponseEntity<Object> createAccount(
		@RequestBody Map<String, Object> requestBody
	) {
		// Checks whether the username already exists in the database
		Optional<Account> existsAccount = accountService.findAccountByUsername((String) requestBody.get("username"));
		if (existsAccount.isPresent())
			throw new AccountExistsException();

		Account account = new Account();
		account.setUsername((String) requestBody.get("username"));
		account.setPassword((String) requestBody.get("password"));
		account.setCreatedAt(LocalDateTime.now());
		account.setUpdatedAt(LocalDateTime.now());
		accountService.save(account);

		final String TOKEN = "Bearer " + account.getAccountId();
		return ResponseEntity.status(HttpStatus.CREATED).body(
			Map.of(
				"userId", account.getAccountId(),
				"username", account.getUsername(),
				"token", TOKEN
			)
		);
	}
}

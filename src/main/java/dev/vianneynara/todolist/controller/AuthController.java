package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AccountService accountService;

	public AuthController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/request-token")
	public ResponseEntity<Object> login(
		@RequestBody Map<String, Object> requestBody
	) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername((String) requestBody.get("username"));

		if (accountQueryResult.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}

		if (!accountQueryResult.get().getPassword().equals((String) requestBody.get("password"))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
		}

		final String TOKEN = "Bearer " + accountQueryResult.get().getAccountId();
		return ResponseEntity
			.status(HttpStatus.OK)
			.header("Content-Type", "application/json")
			.body(Map.of("token", TOKEN));
	}
}

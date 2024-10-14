package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.service.AccountService;
import dev.vianneynara.todolist.utils.ResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private final AccountService accountService;

	public AuthController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Token request endpoint.
	 * @param requestBody Request body containing username and password.
	 * @return ResponseEntity containing the token.
	 */
	@PostMapping("/request-token")
	public ResponseEntity<Object> login(
		@RequestBody Map<String, Object> requestBody
	) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername((String) requestBody.get("username"));

		// if the account is not found or the password is incorrect, return 404
		if (accountQueryResult.isEmpty() || !accountQueryResult.get().getPassword().equals(requestBody.get("password"))) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessages.USER_NOT_FOUND);
		}

		final String TOKEN = "Bearer " + accountQueryResult.get().getAccountId();
		return ResponseEntity
			.status(HttpStatus.OK)
			.header("Content-Type", "application/json")
			.body(Map.of("token", TOKEN));
	}
}

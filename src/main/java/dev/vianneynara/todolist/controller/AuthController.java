package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
		final String TOKEN = accountService.requestToken(
			(String) requestBody.get("username"),
			(String) requestBody.get("password")
		);
		return ResponseEntity.status(HttpStatus.OK)
			.header("Content-Type", "application/json")
			.body(Map.of("token", TOKEN));
	}
}

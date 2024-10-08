package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Object> getUsers(@RequestHeader(name = "System-PIN") String h_systemPin) {
		final String systemPin = "todolist";
		if (!(systemPin.equals(h_systemPin))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

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
}

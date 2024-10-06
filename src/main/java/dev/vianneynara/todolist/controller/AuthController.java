package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.service.UsersService;
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

	private final UsersService usersService;

	public AuthController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping("/request-token")
	public ResponseEntity<String> login(
		@RequestBody Map<String, String> requestBody
		) {

		Optional<Users> user = usersService.findUsersByUsername(requestBody.get("username"));

		if (user.isEmpty()) {
			return ResponseEntity.status(404).body("User not found!");
		}

		if (!user.get().getPassword().equals(requestBody.get("password"))) {
			return ResponseEntity.status(401).body("Incorrect password!");
		}

		// returns the id (now acts as a token)
		return ResponseEntity.ok(String.valueOf(user.get().getUserId()));
	}
}

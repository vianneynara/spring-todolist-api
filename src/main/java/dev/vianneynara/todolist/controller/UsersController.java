package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.dto.UserSimpleDto;
import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UsersController {

	private final UsersService usersService;

	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	/** Speaking method, used to test Spring Boot application context */
	public String speak() {
		return "I am ".concat(this.getClass().getName()) + "!";
	}

	/**
	 * Gather all users in the database.
	 * @return all users.
	 */
	@GetMapping("/all-users")
	public ResponseEntity<Object> getUsers(@RequestHeader(name = "System-PIN") String h_systemPin) {
		final String systemPin = "todolist";
		if (!(systemPin.equals(h_systemPin))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

//		List<UserSimpleDto> users = usersService.findAllUserSimpleDtos();

		List<Users> users = (List<Users>) usersService.findAll();
		Set<Map<String, Object>> mappedUsers = new HashSet<>();
		for (Users user : users) {
		   Map<String, Object> mappedUser = new LinkedHashMap<>();
		   mappedUser.put("userId", user.getUserId());
		   mappedUser.put("username", user.getUsername());
		   mappedUser.put("password", user.getPassword());
		   mappedUser.put("tasks", user.getTasks().size());
		   mappedUser.put("createdAt", user.getCreatedAt());
		   mappedUser.put("updatedAt", user.getUpdatedAt());
		   mappedUsers.add(mappedUser);
		}
		return ResponseEntity.ok(mappedUsers);
	}
}

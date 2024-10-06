package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.service.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UsersController {

	private final UsersService usersService;

	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping("/all-users")
	public Iterable<Users> getUsers() {
		return usersService.findAll();
	}
}

package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Users;

import java.util.Optional;

public interface UsersService {

	Iterable<Users> findAll();

	Optional<Users> findUsersByUsername(String username);
}

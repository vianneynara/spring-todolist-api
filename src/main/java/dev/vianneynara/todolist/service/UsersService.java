package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Users;

public interface UsersService {

	Iterable<Users> findAll();
}

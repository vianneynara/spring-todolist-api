package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.dto.UserSimpleDto;
import dev.vianneynara.todolist.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UsersService {

	Iterable<Users> findAll();

//	List<UserSimpleDto> findAllUserSimpleDtos();

	Optional<Users> findById(Long userId);

	Optional<Users> findUsersByUsername(String username);
}

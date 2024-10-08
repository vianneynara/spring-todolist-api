package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.dto.UserSimpleDto;
import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

	private final UsersRepository usersRepository;

	public UsersServiceImpl(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	public Iterable<Users> findAll() {
		return usersRepository.findAll();
	}

	@Override
	public Optional<Users> findById(Long userId) {
		return usersRepository.findById(userId);
	}

	@Override
	public Optional<Users> findUsersByUsername(String username) {
		return usersRepository.findUsersByUsername(username);
	}

//	@Override
//	public List<UserSimpleDto> findAllUserSimpleDtos() {
//		return usersRepository.findAllUserSimpleDtos();
//	}
}

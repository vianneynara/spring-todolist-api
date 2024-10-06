package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.repository.UsersRepository;
import org.springframework.stereotype.Service;

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
}

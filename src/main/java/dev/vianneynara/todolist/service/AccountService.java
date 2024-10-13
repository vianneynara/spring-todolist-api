package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;

import java.util.Optional;

public interface AccountService {

	Iterable<Account> findAll();

	Account save(Account account);

//	List<UserSimpleDto> findAllUserSimpleDtos();

	Optional<Account> findById(Long userId);

	Optional<Account> findAccountByUsername(String username);
}

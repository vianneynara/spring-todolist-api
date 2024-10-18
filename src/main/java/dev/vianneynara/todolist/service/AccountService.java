package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;

import java.util.Optional;

public interface AccountService {

	Iterable<Account> findAll();

	Account save(Account account);

	Account findById(Long userId);

	Account findAccountByUsername(String username);

	// Non repository method

	boolean accountExists(String username);

	Account authenticateToken(String token, String username);

	String requestToken(String username, String password);
}

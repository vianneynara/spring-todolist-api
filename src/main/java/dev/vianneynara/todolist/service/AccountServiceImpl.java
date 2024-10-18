package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.exceptions.AccountNotFoundException;
import dev.vianneynara.todolist.exceptions.InvalidCredentialsException;
import dev.vianneynara.todolist.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // this annotation indicates the context as a service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Account save(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Iterable<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account findById(Long userId) {
		Optional<Account> accountQueryResult = accountRepository.findById(userId);
		if (accountQueryResult.isEmpty())
			throw new AccountNotFoundException();

		return accountQueryResult.get();
	}

	@Override
	public Account findAccountByUsername(String username) {
		Optional<Account> accountQueryResult = accountRepository.findAccountByUsername(username);
		if (accountQueryResult.isEmpty())
			throw new AccountNotFoundException();

		return accountQueryResult.get();
	}

	@Override
	public boolean accountExists(String username) {
		return accountRepository.findAccountByUsername(username).isPresent();
	}

	@Override
	public Account authenticateToken(String token, String username) {
		Account account = findAccountByUsername(username);

		// simple token authentication by comparing the token with the account ID
		if (!token.equals("Bearer " + account.getAccountId()))
			throw new InvalidCredentialsException();

		// if the token is valid, the method will return the account
		return account;
	}

	@Override
	public String requestToken(String username, String password) {
		Account account = findAccountByUsername(username);

		// if the password is incorrect, throw an exception
		if (!account.getPassword().equals(password))
			throw new InvalidCredentialsException();

		// if the password is correct, return the token
		return "Bearer " + account.getAccountId();
	}
}

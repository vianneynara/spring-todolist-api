package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
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
	public Optional<Account> findById(Long userId) {
		return accountRepository.findById(userId);
	}

	@Override
	public Optional<Account> findAccountByUsername(String username) {
		return accountRepository.findAccountByUsername(username);
	}
}

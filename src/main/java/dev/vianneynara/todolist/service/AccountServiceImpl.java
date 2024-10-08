package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
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

//	@Override
//	public List<UserSimpleDto> findAllUserSimpleDtos() {
//		return usersRepository.findAllUserSimpleDtos();
//	}
}

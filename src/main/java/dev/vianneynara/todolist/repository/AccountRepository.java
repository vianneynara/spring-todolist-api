package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

	Optional<Account> findAccountByUsername(String username);
}

package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Long> {

	Optional<Users> findUsersByUsername(String username);
}

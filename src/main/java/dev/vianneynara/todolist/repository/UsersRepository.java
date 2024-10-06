package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Users;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
}

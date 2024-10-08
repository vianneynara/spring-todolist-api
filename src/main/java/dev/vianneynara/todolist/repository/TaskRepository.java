package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {

	List<Task> findByAccount_AccountId(Long userId);

	List<Task> findByAccount_Username(String username);
}

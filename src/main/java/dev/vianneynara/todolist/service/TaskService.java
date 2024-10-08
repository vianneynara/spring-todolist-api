package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

	Task save(Task task);

	Iterable<Task> findAll();

	Optional<Task> findById(Long taskId);

	List<Task> findByAccount_AccountId(Long userId);

	List<Task> findByAccount_Username(String username);

	boolean deleteById(Long taskId);
}

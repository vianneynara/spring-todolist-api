package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.entity.Task;

import java.util.List;

public interface TaskService {

	Task save(Task task);

	Task save(String title, String deadline, Account account);

	Task update(Long taskId, Account account, String title, String deadline, boolean isCompleted);

	Iterable<Task> findAll();

	Task findById(Long taskId);

	List<Task> findByAccount_Username(String username);

	boolean deleteById(Long taskId);

	void toggleCompletion(Long taskId);

	void changeDeadline(Long taskId, String deadline);
}

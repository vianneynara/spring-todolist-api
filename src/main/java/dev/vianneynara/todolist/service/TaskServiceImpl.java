package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.repository.TaskRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // this annotation indicates the context as a service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;

	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task save(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Optional<Task> findById(Long taskId) {
		return taskRepository.findById(taskId);
	}

	@Override
	public List<Task> findByAccount_AccountId(Long userId) {
		return taskRepository.findByAccount_AccountId(userId);
	}

	@Override
	public List<Task> findByAccount_Username(String username) {
		return taskRepository.findByAccount_Username(username);
	}

	@Override
	public boolean deleteById(Long taskId) {
		// had to double-check to confirm whether it exists in the database
		if (taskRepository.existsById(taskId)) {
			taskRepository.deleteById(taskId);
			return true;
		}
		return false;
	}
}

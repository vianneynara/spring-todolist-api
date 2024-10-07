package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.repository.TasksRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// this annotation indicates the context as a service
@Service
public class TasksServiceImpl implements TasksService {

	private final TasksRepository tasksRepository;

	public TasksServiceImpl(TasksRepository tasksRepository) {
		this.tasksRepository = tasksRepository;
	}

	@Override
	public Tasks save(Tasks task) {
		return tasksRepository.save(task);
	}

	@Override
	public Iterable<Tasks> findAll() {
		return tasksRepository.findAll();
	}

	@Override
	public Optional<Tasks> findById(Long taskId) {
		return tasksRepository.findById(taskId);
	}

	@Override
	public List<Tasks> findByUser_UserId(Long userId) {
		return tasksRepository.findByUser_UserId(userId);
	}

	@Override
	public boolean deleteById(Long taskId) {
		// had to double check to confirm whether it exists in the database
		if (tasksRepository.existsById(taskId)) {
			tasksRepository.deleteById(taskId);
			return true;
		}
		return false;
	}
}

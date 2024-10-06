package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.repository.TasksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// this annotation indicates the context as a service
@Service
public class TasksServiceImpl implements TasksService {

	private final TasksRepository tasksRepository;

	public TasksServiceImpl(TasksRepository tasksRepository) {
		this.tasksRepository = tasksRepository;
	}

	@Override
	public Iterable<Tasks> findAll() {
		return tasksRepository.findAll();
	}

	@Override
	public List<Tasks> findByUser_UserId(Long userId) {
		return tasksRepository.findByUser_UserId(userId);
	}
}

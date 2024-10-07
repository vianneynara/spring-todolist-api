package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;

import java.util.List;
import java.util.Optional;

public interface TasksService {

	Tasks save(Tasks task);

	Iterable<Tasks> findAll();

	Optional<Tasks> findById(Long taskId);

	List<Tasks> findByUser_UserId(Long userId);

	boolean deleteById(Long taskId);
}

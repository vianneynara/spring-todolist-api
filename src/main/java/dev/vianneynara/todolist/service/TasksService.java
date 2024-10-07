package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface TasksService {

	Iterable<Tasks> findAll();

	Optional<Tasks> findById(Long taskId);

	List<Tasks> findByUser_UserId(Long userId);

	boolean deleteById(Long taskId);
}

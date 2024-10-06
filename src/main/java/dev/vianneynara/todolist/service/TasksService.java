package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;

import java.util.List;

public interface TasksService {

	Iterable<Tasks> findAll();

	List<Tasks> findByUser_UserId(Long userId);
}

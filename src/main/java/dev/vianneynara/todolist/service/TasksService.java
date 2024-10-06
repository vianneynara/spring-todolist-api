package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Tasks;

public interface TasksService {

	Iterable<Tasks> findAll();
}

package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.service.TasksService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TasksController {

	private final TasksService tasksService;

	public TasksController(TasksService tasksService) {
		this.tasksService = tasksService;
	}

	@GetMapping("/tasks")
	public Iterable<Tasks> getTasks() {
		return tasksService.findAll();
	}
}

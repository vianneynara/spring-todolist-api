package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.service.TasksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TasksController {

	private final TasksService tasksService;

	public TasksController(TasksService tasksService) {
		this.tasksService = tasksService;
	}

	@GetMapping("/all-tasks")
	public Iterable<Tasks> getTasks() {
		return tasksService.findAll();
	}

	/**
	 * Create get routing that asks for request header or body of user_id.
	 * Will return all tasks that are associated with that user_id.
	 * @param h_userId header `User-Identifier` that contains user id
	 * @return Mapped JSON response.
	 */
	@GetMapping("/tasks")
	public ResponseEntity<Map<String, List<Tasks>>> getTasksByUserId
	(
		@RequestHeader(value = "User-Identifier", required = true) Long h_userId
	) {

		if (h_userId == null) {
			// return bad request if user id is not provided
			return ResponseEntity.badRequest().build();
		}

		// query tasks that are associated with the user id
		final List<Tasks> tasks = tasksService.findByUser_UserId(h_userId);
		return ResponseEntity.ok(Map.of("data", tasks));
	}
}

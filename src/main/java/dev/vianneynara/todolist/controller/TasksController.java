package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.exceptions.ResourceNotFoundException;
import dev.vianneynara.todolist.service.TasksService;
import dev.vianneynara.todolist.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TasksController {

	private final TasksService tasksService;
	private final UsersService usersService;

	public TasksController(TasksService tasksService, UsersService usersService) {
		this.tasksService = tasksService;
		this.usersService = usersService;
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
		@RequestHeader(value = "User-Token", required = true) final Long h_userId
	) {

		if (h_userId == null) {
			// return bad request if user id is not provided
			return ResponseEntity.badRequest().build();
		}

		// query tasks that are associated with the user id
		final List<Tasks> tasks = tasksService.findByUser_UserId(h_userId);
		return ResponseEntity.ok(Map.of("data", tasks));
	}

	@DeleteMapping("/tasks")
	public ResponseEntity<String> deleteTaskByUserId
	(
		@RequestHeader(value = "User-Token", required = true) final Long h_userId,
		@RequestParam(name = "taskId", required = true) final Long p_taskId
	) {

		// get user with corresponding token (user id now)
		Optional<Users> user = usersService.findById(h_userId);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		// get task id
		Optional<Tasks> task = tasksService.findById(p_taskId);
		if (task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}

		// verify ownership
		if (!(task.get().getUser().getUserId().equals(user.get().getUserId()))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		// return Successfully deleted if resource exists
		if (tasksService.deleteById(p_taskId)) {
			return ResponseEntity.ok("Successfully deleted");
		} else {
			return ResponseEntity.ok("Task not found");
		}
	}
}

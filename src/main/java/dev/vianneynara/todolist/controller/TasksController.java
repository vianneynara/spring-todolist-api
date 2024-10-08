package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.service.TasksService;
import dev.vianneynara.todolist.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

	/** Speaking method, used to test Spring Boot application context */
	public String speak() {
		return "I am ".concat(this.getClass().getName()) + "!";
	}

	/**
	 * Gather all tasks in the database.
	 * @return all tasks.
	 */
	@GetMapping("/all-tasks")
	public ResponseEntity<Object> getTasks(@RequestHeader(name = "System-PIN") String h_systemPin) {
		String systemPin = "todolist";
		if (!(systemPin.equals(h_systemPin))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		Iterable<Tasks> tasks = tasksService.findAll();
		return ResponseEntity.ok(tasks);
	}

	/**
	 * GET routing that asks for request header of token used to access a user's tasks.
	 * Will return all tasks that are associated with that user_id.
	 *
	 * @param h_userId header `User-Identifier` that contains user id
	 * @return mapped JSON response.
	 */
	@GetMapping("/tasks")
	public ResponseEntity<Map<String, List<Tasks>>> getTasksByUserId(
		@RequestHeader(value = "User-Token", required = true) final Long h_userId
	) {
		// query tasks that are associated with the user id
		final List<Tasks> tasks = tasksService.findByUser_UserId(h_userId);
		return ResponseEntity.ok(Map.of("data", tasks));
	}

	/**
	 * POST routing to create a new task.
	 *
	 * @param h_userId header `User-Identifier` that contains user id.
	 * @param requestBody the request body that must include.
	 * @return response message with data of the newly created task.
	 */
	@PostMapping("/tasks")
	public ResponseEntity<Object> createTask(
		@RequestHeader(value = "User-Token", required = true) final Long h_userId,
		@RequestBody final Map<String, Object> requestBody
	) {
		Optional<Users> user = usersService.findById(h_userId);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		final String title = (String) requestBody.get("title");
		final LocalDate deadLine = LocalDate.parse((String) requestBody.get("deadLine"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		try {
			Tasks task = new Tasks();
			task.setTitle(title);
			task.setDeadline(deadLine);
			task.setUser(user.get());
			task = tasksService.save(task);
			Map<String, Object> responseBody = Map.of(
				"message", "Successfully created",
				"data", Map.of(
					"taskId", task.getTaskId(),
					"title", task.getTitle(),
					"deadLine", task.getDeadline(),
					"isCompleted", task.getCompleted()
				)
			);
			return ResponseEntity.ok(responseBody);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request body");
		}
	}

	/**
	 * Deletes a task of a user.
	 *
	 * @param h_userId user identifier or the token.
	 * @param p_taskId the task identifier.
	 * @return 200 status of "Successfully deleted" or "Task not found"
	 */
	@DeleteMapping("/tasks")
	public ResponseEntity<String> deleteTaskByUserId(
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

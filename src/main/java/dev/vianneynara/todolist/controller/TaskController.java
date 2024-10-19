package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.dto.TaskDto;
import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.exceptions.UnauthorizedException;
import dev.vianneynara.todolist.service.AccountService;
import dev.vianneynara.todolist.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

	private final TaskService taskService;
	private final AccountService accountService;

	public TaskController(TaskService taskService, AccountService accountService) {
		this.taskService = taskService;
		this.accountService = accountService;
	}

	/**
	 * Speaking method, used to test Spring Boot application context
	 */
	public String speak() {
		return "I am ".concat(this.getClass().getName()) + "!";
	}

	/**
	 * Gather all tasks in the database.
	 *
	 * @return all tasks.
	 */
	@GetMapping("/all-tasks")
	public ResponseEntity<Object> getTasks(@RequestHeader(name = "System-PIN") String h_systemPin) {
		if (!("todolist".equals(h_systemPin)))
			throw new UnauthorizedException();

		Iterable<Task> tasks = taskService.findAll();
		return ResponseEntity.ok(tasks);
	}

	/**
	 * GET routing that asks for request header of token used to access a user's tasks.
	 * Will return all tasks that are associated with that user_id.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @return mapped JSON response.
	 */
	@GetMapping("/accounts/{username}/tasks")
	public ResponseEntity<Map<String, List<Task>>> getTasksByUserId(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username
	) {
		accountService.authenticateToken(h_authorization, username);

		// query tasks that are associated with the user id
		final List<Task> tasks = taskService.findByAccount_Username(username);
		return ResponseEntity.ok(Map.of("tasks", tasks));
	}

	/**
	 * POST routing to create a new task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param requestBody     the request body that must include.
	 * @return response message with data of the newly created task.
	 */
	@PostMapping("/accounts/{username}/tasks")
	public ResponseEntity<Object> createTask(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@RequestBody final Map<String, Object> requestBody
	) {
		final Account account = accountService.authenticateToken(h_authorization, username);

		final Task task = taskService.save(
			(String) requestBody.get("title"),
			(String) requestBody.get("deadline"),
			account
		);

		Map<String, Object> responseBody = Map.of(
			"message", "Task successfully created",
			"task", new TaskDto(task)
		);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
	}

	/**
	 * Deletes a task of a user.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param taskId          the task identifier.
	 * @return `200` status of "Successfully deleted" or `404` of "Task not found"
	 */
	@DeleteMapping("/accounts/{username}/tasks/{taskId}")
	public ResponseEntity<Object> deleteTaskByUserId(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId
	) {
		accountService.authenticateToken(h_authorization, username);

		boolean deleted = taskService.deleteById(taskId);
		if (deleted) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}
	}

	/**
	 * PUT routing to update a task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param username        the username of the account.
	 * @param taskId          the task identifier.
	 * @param requestBody     the request body that must include `title`, `deadline`, and `isCompleted`.
	 * @return `204` if successful, `400` if invalid request body value, `404` if task not found.
	 */
	@PutMapping("/accounts/{username}/tasks/{taskId}")
	public ResponseEntity<Object> updateTask(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId,
		@RequestBody final Map<String, Object> requestBody
	) {
		final Account account = accountService.authenticateToken(h_authorization, username);

		final Task task = taskService.update(
			taskId,
			account,
			(String) requestBody.get("title"),
			(String) requestBody.get("deadline"),
			(Boolean) requestBody.get("isCompleted")
		);

		Map<String, Object> responseBody = Map.of(
			"message", "Task successfully updated",
			"task", new TaskDto(task)
		);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseBody);
	}

	/**
	 * PATCH routing to toggle the completion of a task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param username        the username of the account.
	 * @param taskId          the task identifier.
	 * @return response message.
	 */
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", allowedHeaders = "*")
	@PatchMapping("/accounts/{username}/tasks/{taskId}/toggle-completion")
	public ResponseEntity<Object> toggleTaskCompletion(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId
	) {
		accountService.authenticateToken(h_authorization, username);

		taskService.toggleCompletion(taskId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	/**
	 * PATCH routing to change the deadline of a task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param username        the username of the account.
	 * @param taskId          the task identifier.
	 * @param requestBody     the request body that must include `deadline` with `yyyy/MM/dd` format.
	 * @return `204` if successful, `404` if task not found.
	 */
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", allowedHeaders = "*")
	@PatchMapping("/accounts/{username}/tasks/{taskId}/change-deadline")
	public ResponseEntity<Object> changeTaskDeadline(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId,
		@RequestBody final Map<String, Object> requestBody
	) {
		accountService.authenticateToken(h_authorization, username);

		taskService.changeDeadline(taskId, (String) requestBody.get("deadline"));

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Successfully changed deadline"));
	}
}

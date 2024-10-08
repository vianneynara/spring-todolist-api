package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.service.TaskService;
import dev.vianneynara.todolist.service.AccountService;
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
public class TaskController {

	private final TaskService taskService;
	private final AccountService accountService;

	public TaskController(TaskService taskService, AccountService accountService) {
		this.taskService = taskService;
		this.accountService = accountService;
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

		Iterable<Task> tasks = taskService.findAll();
		return ResponseEntity.ok(tasks);
	}

	/**
	 * GET routing that asks for request header of token used to access a user's tasks.
	 * Will return all tasks that are associated with that user_id.
	 *
	 * @param h_accountId header `Account-Token` that contains user id
	 * @return mapped JSON response.
	 */
	@GetMapping("/tasks")
	public ResponseEntity<Map<String, List<Task>>> getTasksByUserId(
		@RequestHeader(value = "Account-Token") final Long h_accountId
	) {
		// query tasks that are associated with the user id
		final List<Task> tasks = taskService.findByAccount_AccountId(h_accountId);
		return ResponseEntity.ok(Map.of("data", tasks));
	}

	/**
	 * POST routing to create a new task.
	 *
	 * @param h_accountId header `User-Identifier` that contains user id.
	 * @param requestBody the request body that must include.
	 * @return response message with data of the newly created task.
	 */
	@PostMapping("/tasks")
	public ResponseEntity<Object> createTask(
		@RequestHeader(value = "Account-Token") final Long h_accountId,
		@RequestBody final Map<String, Object> requestBody
	) {
		Optional<Account> user = accountService.findById(h_accountId);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		final String title = (String) requestBody.get("title");
		final LocalDate deadLine = LocalDate.parse((String) requestBody.get("deadLine"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		try {
			Task task = new Task();
			task.setTitle(title);
			task.setDeadline(deadLine);
			task.setAccount(user.get());
			task = taskService.save(task);
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
	 * @param h_accountId user identifier or the token.
	 * @param p_taskId the task identifier.
	 * @return 200 status of "Successfully deleted" or "Task not found"
	 */
	@DeleteMapping("/tasks")
	public ResponseEntity<String> deleteTaskByUserId(
		@RequestHeader(value = "Account-Token", required = true) final Long h_accountId,
		@RequestParam(name = "taskId", required = true) final Long p_taskId
	) {

		// get user with corresponding token (user id now)
		Optional<Account> user = accountService.findById(h_accountId);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		// get task id
		Optional<Task> task = taskService.findById(p_taskId);
		if (task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}

		// verify ownership
		if (!(task.get().getAccount().getAccountId().equals(user.get().getAccountId()))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		// return Successfully deleted if resource exists
		if (taskService.deleteById(p_taskId)) {
			return ResponseEntity.ok("Successfully deleted");
		} else {
			return ResponseEntity.ok("Task not found");
		}
	}
}

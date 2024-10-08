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
		if (!("todolist".equals(h_systemPin))) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		Iterable<Task> tasks = taskService.findAll();
		return ResponseEntity.ok(tasks);
	}

	/**
	 * GET routing that asks for request header of token used to access a user's tasks.
	 * Will return all tasks that are associated with that user_id.
	 *
	 * @param h_accountToken header `Account-Token` that contains user id
	 * @return mapped JSON response.
	 */
	@GetMapping("/accounts/{username}/tasks")
	public ResponseEntity<Map<String, List<Task>>> getTasksByUserId(
		@RequestHeader(value = "Account-Token") final Long h_accountToken,
		@PathVariable("username") final String username
	) {
		/* simple auth logic */ if (!isAuthorized(h_accountToken, username)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		// query tasks that are associated with the user id
		final List<Task> tasks = taskService.findByAccount_Username(username);
		return ResponseEntity.ok(Map.of("data", tasks));
	}

	/**
	 * POST routing to create a new task.
	 *
	 * @param h_accountToken header `User-Identifier` that contains user id.
	 * @param requestBody the request body that must include.
	 * @return response message with data of the newly created task.
	 */
	@PostMapping("/accounts/{username}/tasks")
	public ResponseEntity<Object> createTask(
		@RequestHeader(value = "Account-Token") final Long h_accountToken,
		@PathVariable("username") final String username,
		@RequestBody final Map<String, Object> requestBody
	) {
		Optional<Account> account = accountService.findAccountByUsername(username);
		if (account.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		/* simple auth logic */ if (!isAuthorized(h_accountToken, account.get().getAccountId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		final String title = (String) requestBody.get("title");
		final LocalDate deadLine = LocalDate.parse((String) requestBody.get("deadLine"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		try {
			Task task = new Task();
			task.setTitle(title);
			task.setDeadline(deadLine);
			task.setAccount(account.get());
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
	 * @param h_accountToken user identifier or the token.
	 * @param taskId the task identifier.
	 * @return 200 status of "Successfully deleted" or "Task not found"
	 */
	@DeleteMapping("/accounts/{username}/tasks/{taskId}")
	public ResponseEntity<String> deleteTaskByUserId(
		@RequestHeader(value = "Account-Token") final Long h_accountToken,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId
	) {
		Optional<Account> account = accountService.findAccountByUsername(username);
		if (account.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		/* simple auth logic */ if (!isAuthorized(h_accountToken, account.get().getAccountId())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		// get task id
		Optional<Task> task = taskService.findById(taskId);
		if (task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
		}

		// verify ownership
		if (!(task.get().getAccount().getAccountId().equals(account.get().getAccountId()))) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
		}

		// return Successfully deleted if resource exists
		if (taskService.deleteById(taskId)) {
			return ResponseEntity.ok("Successfully deleted");
		} else {
			return ResponseEntity.ok("Task not found");
		}
	}

	private boolean isAuthorized(Long h_accountToken, Long accountId) {
		return h_accountToken.equals(accountId);
	}

	private boolean isAuthorized(Long h_accountToken, String username) {
		Optional<Account> account = accountService.findAccountByUsername(username);
		if (account.isEmpty()) {
//			throw new ResourceNotFoundException("Account not found");
			return false;
		}
		return isAuthorized(h_accountToken, account.get().getAccountId());
	}
}

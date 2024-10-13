package dev.vianneynara.todolist.controller;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.exceptions.InvalidRequestBodyValue;
import dev.vianneynara.todolist.exceptions.TaskNotFoundException;
import dev.vianneynara.todolist.exceptions.UnauthorizedException;
import dev.vianneynara.todolist.exceptions.UserNotFoundException;
import dev.vianneynara.todolist.service.AccountService;
import dev.vianneynara.todolist.service.TaskService;
import dev.vianneynara.todolist.utils.ResponseMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
		if (!("todolist".equals(h_systemPin))) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

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
		checkAccountExistsAndTokenIsAuthorized(h_authorization, username);

		// query tasks that are associated with the user id
		final List<Task> tasks = taskService.findByAccount_Username(username);
		return ResponseEntity.ok(Map.of("data", tasks));
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
		final Account account = checkAccountExistsAndTokenIsAuthorized(h_authorization, username);

		if (requestBody.get("title") == null || requestBody.get("deadline") == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessages.INVALID_REQUEST_BODY_VALUE);
		}

		try {
			final String title = (String) requestBody.get("title");
			final LocalDate deadline = LocalDate.parse((String) requestBody.get("deadline"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

			Task task = new Task();
			task.setTitle(title);
			task.setDeadline(deadline);
			task.setAccount(account);
			task = taskService.save(task);
			Map<String, Object> responseBody = Map.of(
				"message", "Task successfully created",
				"task", Map.of(
					"taskId", task.getTaskId(),
					"title", task.getTitle(),
					"deadline", task.getDeadline(),
					"isCompleted", task.getCompleted()
				)
			);
			return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
		} catch (DateTimeParseException e) {
			throw new InvalidRequestBodyValue("A value's format is invalid");
		}
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
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		checkAccountExistsAndTokenIsAuthorized(h_authorization, accountQueryResult);

		// get task id
		Optional<Task> task = taskService.findById(taskId);
		if (task.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessages.TASK_NOT_FOUND);
		}

		// return Successfully deleted if resource exists
		if (taskService.deleteById(taskId)) {
			return ResponseEntity.ok(ResponseMessages.TASK_SUCCESSFULLY_DELETED);
		} else {
			throw new TaskNotFoundException();
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
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		checkAccountExistsAndTokenIsAuthorized(h_authorization, accountQueryResult);

		Optional<Task> task = taskService.findById(taskId);
		if (task.isEmpty()) {
			throw new TaskNotFoundException();
		}

		if (requestBody.get("title") == null || requestBody.get("deadline") == null || requestBody.get("isCompleted") == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessages.INVALID_REQUEST_BODY_VALUE);
		}
		try {
			final String title = ((String) requestBody.get("title")).strip();
			if (title.isEmpty()) throw new InvalidRequestBodyValue("Title is required");
			task.get().setTitle(title);
			task.get().setDeadline(LocalDate.parse((String) requestBody.get("deadline"), DateTimeFormatter.ofPattern("yyyy/MM/dd")));
			task.get().setCompleted((boolean) requestBody.get("isCompleted"));
			taskService.save(task.get());
		} catch (DateTimeParseException | ClassCastException e) {
			throw new InvalidRequestBodyValue("A value's format is invalid");
		}

		return ResponseEntity.ok(ResponseMessages.TASK_SUCCESSFULLY_UPDATED);
	}

	/**
	 * PATCH routing to toggle the completion of a task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param username        the username of the account.
	 * @param taskId          the task identifier.
	 * @return response message.
	 */
	@PatchMapping("/accounts/{username}/tasks/{taskId}/toggle-completion")
	public ResponseEntity<Object> toggleTaskCompletion(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId
	) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		checkAccountExistsAndTokenIsAuthorized(h_authorization, accountQueryResult);

		Optional<Task> task = taskService.findById(taskId);
		if (task.isEmpty()) {
			throw new TaskNotFoundException();
		}

		task.get().setCompleted(!task.get().getCompleted());
		taskService.save(task.get());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Successfully toggled completion"));
	}

	/**
	 * PATCH routing to change the deadline of a task.
	 *
	 * @param h_authorization header `Authorization` that contains authorization token.
	 * @param username        the username of the account.
	 * @param taskId          the task identifier.
	 * @param requestBody     the request body that must include.
	 * @return `204` if successful, `404` if task not found.
	 */
	@PatchMapping("/accounts/{username}/tasks/{taskId}/change-deadline")
	public ResponseEntity<Object> changeTaskDeadline(
		@RequestHeader(value = "Authorization") final String h_authorization,
		@PathVariable("username") final String username,
		@PathVariable("taskId") final Long taskId,
		@RequestBody final Map<String, Object> requestBody
	) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		checkAccountExistsAndTokenIsAuthorized(h_authorization, accountQueryResult);

		Optional<Task> task = taskService.findById(taskId);
		if (task.isEmpty()) {
			throw new TaskNotFoundException();
		}

		final LocalDate newDeadline = LocalDate.parse((String) requestBody.get("deadline"), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		task.get().setDeadline(newDeadline);
		taskService.save(task.get());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("message", "Successfully changed deadline"));
	}

	/**
	 * Custom method to check whether the account query result is empty or not.
	 * It then checks whether the token is authorized for the user or not.
	 *
	 * @param token              the token to be checked.
	 * @param accountQueryResult the account query result.
	 * @return the account if the user exists and is authorized.
	 * @throws UserNotFoundException if the user is not found.
	 * @throws UnauthorizedException if the user is not authorized.
	 */
	private Account checkAccountExistsAndTokenIsAuthorized(String token, Optional<Account> accountQueryResult)
		throws UserNotFoundException, UnauthorizedException {
		if (accountQueryResult.isEmpty()) /* simple user checking logic */
			throw new UserNotFoundException();

		authenticateToken(token, accountQueryResult.get());

		return accountQueryResult.get();
	}

	private Account checkAccountExistsAndTokenIsAuthorized(String token, String username) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		return checkAccountExistsAndTokenIsAuthorized(token, accountQueryResult);
	}

	/**
	 * Custom method to authenticate the token.
	 *
	 * @param token   the token to be authenticated.
	 * @param account the account to be authenticated.
	 */
	private void authenticateToken(String token, Account account) {
		String[] auth = token.split(" ");
		if (!(Long.valueOf(auth[1]).equals(account.getAccountId())))
			throw new UnauthorizedException();
	}

	private void authenticateToken(String token, String username) {
		Optional<Account> accountQueryResult = accountService.findAccountByUsername(username);
		if (accountQueryResult.isEmpty())
			throw new UserNotFoundException();
		authenticateToken(token, accountQueryResult.get());
	}
}

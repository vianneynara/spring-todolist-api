package dev.vianneynara.todolist.service;

import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.exceptions.InvalidRequestBodyValue;
import dev.vianneynara.todolist.exceptions.TaskNotFoundException;
import dev.vianneynara.todolist.exceptions.UnauthorizedException;
import dev.vianneynara.todolist.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service // this annotation indicates the context as a service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final AccountService accountService;
	private final Logger logger;

	public TaskServiceImpl(TaskRepository taskRepository, AccountService accountService) {
		this.taskRepository = taskRepository;
		this.accountService = accountService;
		this.logger = Logger.getLogger(TaskServiceImpl.class.getName());
	}

	@Transactional
	@Override
	public Task save(Task task) {
		return taskRepository.save(task);
	}

	@Transactional
	@Override
	public Task save(String title, String deadline, Account account) {
		try {
			final LocalDate dueDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

			Task task = new Task();
			task.setTitle(title);
			task.setDeadline(dueDate);
			task.setAccount(account);
			return taskRepository.save(task);
		} catch (DateTimeParseException e) {
			throw new InvalidRequestBodyValue("A value's format is invalid");
		}
	}

	@Transactional
	@Override
	public Task update(Long taskId, Account account, String title, String deadline, boolean isCompleted) {
		try {
			Task task = taskRepository.findById(taskId)
				.orElse(new Task());

			// validate ownership of the task if the task exists
			if (task.getTaskId() != null && !task.getAccount().equals(account)) {
				throw new UnauthorizedException("You don't have permission to update this task");
			}

			task.setAccount(account);
			task.setTitle(title);
			task.setDeadline(LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy/MM/dd")));
			task.setCompleted(isCompleted);

			return taskRepository.save(task);
		} catch (DateTimeParseException e) {
			throw new InvalidRequestBodyValue("Invalid date format. Expected yyyy/MM/dd");
		}
	}

	@Override
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}

	@Override
	public Task findById(Long taskId) {
		Optional<Task> taskQueryResult = taskRepository.findById(taskId);
		if (taskQueryResult.isEmpty())
			throw new TaskNotFoundException();

		return taskQueryResult.get();
	}

	@Override
	public List<Task> findByAccount_Username(String username) {
		return taskRepository.findByAccount_Username(username);
	}

	@Transactional
	@Override
	public boolean deleteById(Long taskId) {
		try {
			if (taskRepository.existsById(taskId)) {
				taskRepository.deleteById(taskId);
				return true;
			}
			return false;
		} catch (StaleObjectStateException | OptimisticLockingFailureException e) {
			logger.warning("Attempted to delete an already deleted task: " + taskId);
			return true;
		}
	}

	@Transactional
	@Override
	public void toggleCompletion(Long taskId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(TaskNotFoundException::new);
		task.setCompleted(!task.getCompleted());
		taskRepository.save(task);
	}

	@Transactional
	@Override
	public void changeDeadline(Long taskId, String deadline) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(TaskNotFoundException::new);

		try {
			task.setDeadline(LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy/MM/dd")));
		} catch (DateTimeParseException e) {
			throw new InvalidRequestBodyValue("Invalid date format. Expected yyyy/MM/dd");
		}

		taskRepository.save(task);
	}
}

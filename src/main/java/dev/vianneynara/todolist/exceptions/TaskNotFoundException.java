package dev.vianneynara.todolist.exceptions;

public class TaskNotFoundException extends ResourceNotFoundException {

	public TaskNotFoundException() {
		super("Task not found");
	}

	public TaskNotFoundException(String message) {
		super(message);
	}
}

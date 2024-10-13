package dev.vianneynara.todolist.exceptions;

public class UserNotFoundException extends ResourceNotFoundException {

	public UserNotFoundException() {
		super("User not found");
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}

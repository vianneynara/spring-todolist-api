package dev.vianneynara.todolist.exceptions;

public class AccountNotFoundException extends RuntimeException {

	public AccountNotFoundException() {
		super("Account already exists");
	}

	public AccountNotFoundException(String message) {
		super(message);
	}
}

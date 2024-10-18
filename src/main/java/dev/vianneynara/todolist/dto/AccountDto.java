package dev.vianneynara.todolist.dto;

import dev.vianneynara.todolist.entity.Account;

import java.time.LocalDateTime;

public class AccountDto {
	private Long accountId;
	private String username;
	private int tasksCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Constructor
	public AccountDto(Long accountId, String username, int tasksCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.accountId = accountId;
		this.username = username;
		this.tasksCount = tasksCount;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public AccountDto(Account account) {
		this.accountId = account.getAccountId();
		this.username = account.getUsername();
		this.tasksCount = account.getTasks().size();
		this.createdAt = account.getCreatedAt();
		this.updatedAt = account.getUpdatedAt();
	}

	// Getters and setters

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getTasksCount() {
		return tasksCount;
	}

	public void setTasksCount(int tasksCount) {
		this.tasksCount = tasksCount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
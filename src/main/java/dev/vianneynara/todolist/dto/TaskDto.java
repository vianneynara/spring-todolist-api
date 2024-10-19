package dev.vianneynara.todolist.dto;

import dev.vianneynara.todolist.entity.Task;

public class TaskDto {

	private Long taskId;
	private String title;
	private String deadline;
	private Boolean isCompleted;
	private Long accountId;

	public TaskDto(Long taskId, String title, String deadline, Boolean isCompleted, Long accountId) {
		this.taskId = taskId;
		this.title = title;
		this.deadline = deadline;
		this.isCompleted = isCompleted;
		this.accountId = accountId;
	}

	public TaskDto(Task task) {
		this.taskId = task.getTaskId();
		this.title = task.getTitle();
		this.deadline = task.getDeadline().toString();
		this.isCompleted = task.getCompleted();
		this.accountId = task.getAccount().getAccountId();
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Boolean getCompleted() {
		return isCompleted;
	}

	public void setCompleted(Boolean completed) {
		isCompleted = completed;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}

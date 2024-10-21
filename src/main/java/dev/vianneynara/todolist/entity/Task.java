package dev.vianneynara.todolist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.vianneynara.todolist.utils.idgen.CustomSequence;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Task extends Auditable {

	@Id
	@CustomSequence(name = "snowflake")
	@Column(name = "task_id")
	private Long taskId;

	@Column()
	private String title;

	@Column(name = "deadline")
	private LocalDate deadline;

	@Column(name = "is_completed")
	@ColumnDefault("false")
	private Boolean isCompleted = false;

	@ManyToOne()
	@JoinColumn(name = "account_id")
	@JsonBackReference // the inverse of the relationship (dependant side)
	private Account account = new Account();

	// Getters and setters

	public Long getTaskId() {
		return taskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public Boolean getCompleted() {
		return isCompleted;
	}

	public void setCompleted(Boolean completed) {
		isCompleted = completed;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Task{" +
			"taskId=" + taskId +
			", title='" + title + '\'' +
			", deadline=" + deadline +
			", isCompleted=" + isCompleted +
			", account=" + account +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Task task = (Task) o;
		return Objects.equals(taskId, task.taskId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(taskId);
	}
}

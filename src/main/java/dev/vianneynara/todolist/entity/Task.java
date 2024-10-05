package dev.vianneynara.todolist.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Task extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "task_id")
	private Long taskId;

	private String title;

	private LocalDate deadline;

	private Boolean isCompleted;

	@ManyToOne()
	@JoinTable(
		name = "user_task",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "task_id")
	)
	private User user;

	// Getters and setters

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Task{" +
			"taskId=" + taskId +
			", title='" + title + '\'' +
			", deadline=" + deadline +
			", isCompleted=" + isCompleted +
			", user=" + user +
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

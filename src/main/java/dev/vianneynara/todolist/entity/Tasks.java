package dev.vianneynara.todolist.entity;

import jakarta.persistence.*;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Tasks {

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
		joinColumns = @JoinColumn(name = "task_id"),
		inverseJoinColumns = @JoinColumn(name = "user_id")
	)
	private Users user = new Users();

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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
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

		Tasks task = (Tasks) o;
		return Objects.equals(taskId, task.taskId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(taskId);
	}
}

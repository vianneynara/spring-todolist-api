package dev.vianneynara.todolist.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class User extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username")
	private String username;

	private String password;

	private LocalDate deadline;

	@OneToMany(mappedBy = "user")
	private Set<Task> tasks;

	// Getters and setters

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "User{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", password='" + password + '\'' +
			", deadline=" + deadline +
			", tasks=" + tasks +
			'}';
	}

	/**
	 * This hash code equality helps Hibernate .
	 * @param o the object to be compared.
	 * @return whether it's equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}
}

package dev.vianneynara.todolist.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// i made sure that the name does not create conflict between H2, since "User" is a reserved keyword in H2
@Entity
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username")
	private String username;

	private String password;

	@OneToMany(mappedBy = "user")
	private Set<Tasks> tasks = new HashSet<>();

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

	public Set<Tasks> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Tasks> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "User{" +
			"userId=" + userId +
			", username='" + username + '\'' +
			", password='" + password + '\'' +
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

		Users user = (Users) o;
		return Objects.equals(userId, user.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}
}

package dev.vianneynara.todolist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// i made sure that the name does not create conflict between H2, since "User" is a reserved keyword in H2
@Entity
public class Users extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "username", unique = true)
	private String username;

	@Column()
	private String password;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference // the owner of the relationship
	private Set<Tasks> tasks = new HashSet<>();

	// Getters and setters

	public Long getUserId() {
		return userId;
	}

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

	private int getTasksCount() {
		return tasks.size();
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

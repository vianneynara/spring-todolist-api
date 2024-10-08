package dev.vianneynara.todolist.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Account extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_id")
	private Long accountId;

	@Column(unique = true)
	private String username;

	@Column()
	private String password;

	@OneToMany(mappedBy = "account")
	@JsonManagedReference // the owner of the relationship
	private Set<Task> tasks = new HashSet<>();

	// Getters and setters

	public Long getAccountId() {
		return accountId;
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

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	private int getTasksCount() {
		return tasks.size();
	}

	@Override
	public String toString() {
		return "Account{" +
			"accountId=" + accountId +
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

		Account account = (Account) o;
		return Objects.equals(accountId, account.accountId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(accountId);
	}
}

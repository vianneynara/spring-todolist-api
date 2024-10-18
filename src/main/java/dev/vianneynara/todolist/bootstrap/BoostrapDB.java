package dev.vianneynara.todolist.bootstrap;

import dev.vianneynara.todolist.entity.Task;
import dev.vianneynara.todolist.entity.Account;
import dev.vianneynara.todolist.repository.TaskRepository;
import dev.vianneynara.todolist.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * This class is made to instantiate the database with default contents.
 * It creates objects, saving them, and then adding association between the saved objects.
 * <p>
 * `@Component` annotation declared here to tell spring to detect this as a spring component.
 * It will load this as a spring bean then autowires them in. run method is then run.
 */
@Component
public class BoostrapDB implements CommandLineRunner {

	// immutables, this has not been initialized, so we should add constructor to fill it.
	private final AccountRepository accountRepository;
	private final TaskRepository taskRepository;

	// auto-wiring the repository implementations
	public BoostrapDB(AccountRepository accountRepository, TaskRepository taskRepository) {
		this.accountRepository = accountRepository;
		this.taskRepository = taskRepository;
	}

	// this will be run on application startup
	@Override
	public void run(String... args) throws Exception {
		// author nara
		Account nara = new Account();
		nara.setUsername("nara");
		nara.setPassword("nara");

		Task task1 = new Task();
		task1.setTitle("Nara's Task 1");
		task1.setDeadline(java.time.LocalDate.of(2024, 10, 11));
		task1.setCompleted(false);
		task1.setAccount(nara);

		Task task2 = new Task();
		task2.setTitle("Nara's Task 2");
		task2.setDeadline(java.time.LocalDate.of(2024, 10, 12));
		task2.setCompleted(false);
		task2.setAccount(nara);

		// author emilia
		Account emilia = new Account();
		emilia.setUsername("emilia");
		emilia.setPassword("emilia");

		Task task3 = new Task();
		task3.setTitle("Emilia's Task 1");
		task3.setDeadline(java.time.LocalDate.of(2024, 10, 13));
		task3.setCompleted(false);
		task3.setAccount(emilia);

		// saving the objects to the database
		Account naraSAVED = accountRepository.save(nara);
		Task task1SAVED = taskRepository.save(task1);
		Task task2SAVED = taskRepository.save(task2);

		Account emiliaSAVED = accountRepository.save(emilia);
		Task task3SAVED = taskRepository.save(task3);

		// creating association between user and task interchangeably
		naraSAVED.getTasks().add(task1SAVED);
		naraSAVED.getTasks().add(task2SAVED);
		emiliaSAVED.getTasks().add(task3SAVED);

		// persists/save the changes
		accountRepository.save(naraSAVED);
		accountRepository.save(emiliaSAVED);

		// try logging
		Logger logger = Logger.getLogger(BoostrapDB.class.getName());
		logger.info("Database bootstrapping completed.");
	}
}

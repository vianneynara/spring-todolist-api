package dev.vianneynara.todolist.bootstrap;

import dev.vianneynara.todolist.entity.Tasks;
import dev.vianneynara.todolist.entity.Users;
import dev.vianneynara.todolist.repository.TasksRepository;
import dev.vianneynara.todolist.repository.UsersRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @component annotation declared here to tell spring detect this as a spring bean

/**
 * This class is made to instantiate the database with default contents.
 * It creates objects, saving them, and then adding association between the saved objects.
 */
@Component
public class BoostrapDB implements CommandLineRunner {

	// immutables, this has not been initialized, so we should add constructor to fill it.
	private final UsersRepository usersRepository;
	private final TasksRepository tasksRepository;

	// auto-wiring the repository implementations
	public BoostrapDB(UsersRepository usersRepository, TasksRepository tasksRepository) {
		this.usersRepository = usersRepository;
		this.tasksRepository = tasksRepository;
	}

	// this will be run on application startup
	@Override
	public void run(String... args) throws Exception {
		// author nara
		Users nara = new Users();
		nara.setUsername("nara");
		nara.setPassword("nara");

		Tasks task1 = new Tasks();
		task1.setTitle("Nara's Task 1");
		task1.setDeadline(java.time.LocalDate.now().plusDays(1));
		task1.setCompleted(false);
		task1.setUser(nara);

		Tasks task2 = new Tasks();
		task2.setTitle("Nara's Task 2");
		task2.setDeadline(java.time.LocalDate.now().plusDays(1));
		task2.setCompleted(false);
		task2.setUser(nara);

		// author emilia
		Users emilia = new Users();
		emilia.setUsername("emilia");
		emilia.setPassword("emilia");

		Tasks task3 = new Tasks();
		task3.setTitle("Emilia's Task 1");
		task3.setDeadline(java.time.LocalDate.now().plusDays(1));
		task3.setCompleted(false);
		task3.setUser(emilia);

		// saving the objects to the database
		Users naraSAVED = usersRepository.save(nara);
		Tasks task1SAVED = tasksRepository.save(task1);
		Tasks task2SAVED = tasksRepository.save(task2);

		Users emiliaSAVED = usersRepository.save(emilia);
		Tasks task3SAVED = tasksRepository.save(task3);

		// creating association between user and task
		naraSAVED.getTasks().add(task1SAVED);
		naraSAVED.getTasks().add(task2SAVED);
		emiliaSAVED.getTasks().add(task3SAVED);

		// try logging
		System.out.println("Bootstrap");
		System.out.println("Authors count: " + usersRepository.count());
		System.out.println("Tasks count: " + tasksRepository.count());
	}
}

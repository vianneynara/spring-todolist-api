package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {
}

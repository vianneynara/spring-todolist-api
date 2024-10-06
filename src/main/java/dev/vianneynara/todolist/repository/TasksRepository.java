package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Tasks;
import org.springframework.data.repository.CrudRepository;

public interface TasksRepository extends CrudRepository<Tasks, Long> {
}

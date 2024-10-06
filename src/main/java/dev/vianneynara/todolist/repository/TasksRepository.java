package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.entity.Tasks;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TasksRepository extends CrudRepository<Tasks, Long> {

	List<Tasks> findByUser_UserId(Long userId);
}

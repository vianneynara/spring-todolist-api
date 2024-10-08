package dev.vianneynara.todolist.repository;

import dev.vianneynara.todolist.dto.UserSimpleDto;
import dev.vianneynara.todolist.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Long> {

	Optional<Users> findUsersByUsername(String username);

//	@Query("select new dev.vianneynara.todolist.dto.UserSimpleDto(u.userId, u.username, u.password, size(u.tasks), u.createdAt, u.updatedAt) from Users u")
//	List<UserSimpleDto> findAllUserSimpleDtos();
}

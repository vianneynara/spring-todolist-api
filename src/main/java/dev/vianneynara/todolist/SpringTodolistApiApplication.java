package dev.vianneynara.todolist;

import dev.vianneynara.todolist.controller.TasksController;
import dev.vianneynara.todolist.controller.UsersController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringTodolistApiApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringTodolistApiApplication.class, args);

		TasksController tasksController = ctx.getBean(TasksController.class);
		UsersController usersController = ctx.getBean(UsersController.class);

		System.out.println(tasksController.speak());
		System.out.println(usersController.speak());
	}

}

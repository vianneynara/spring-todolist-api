package dev.vianneynara.todolist;

import dev.vianneynara.todolist.controller.TaskController;
import dev.vianneynara.todolist.controller.AccountController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaAuditing
public class SpringTodolistApiApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringTodolistApiApplication.class, args);

		TaskController taskController = ctx.getBean(TaskController.class);
		AccountController accountController = ctx.getBean(AccountController.class);

		System.out.println(taskController.speak());
		System.out.println(accountController.speak());
	}

	@Bean
    public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/v1/**").allowedOrigins("*");
			}
		};
    }
}

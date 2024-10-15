# Todo List API

Simple tasks project (TODO list) API to practice Spring Boot.

## Main Repository

This project is applied as the providing server of my [Todo Web App](https://github.com/vianneynara/todo-web-app) project.

## Features

- [ ] Account entity.
    - [x] Create an account.
    - [x] Token request by username and password.
    - [ ] Hashed password/token.
- [x] CRUD operations for Task.
    - [x] Create a task.
    - [x] Get tasks of a user.
    - [x] Update a task.
    - [x] Delete a task.
    - [x] Patch task completion
    - [x] Patch task deadline.

## API documentation

Full API documentation can be found in **[this Postman documentation
](https://documenter.getpostman.com/view/20896360/2sAXxS9Bos)**.

## Prerequisites

- Java JDK 21 (This project has not been compiled to JAR yet)
- Maven 3.9.9+

## Installing and running application

Default configurations:
- Port: 8081
- Database: H2
- Base URL: http://localhost:8081/api/v1

1. Clone the repository
```shell
git clone https://github.com/vianneynara/spring-todolist-api.git
```

2. Navigate to the project's folder
```shell
cd spring-todolist-api
```

3. Run the application with Maven
```shell
mvn spring-boot:run
```

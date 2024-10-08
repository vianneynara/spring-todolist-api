package dev.vianneynara.todolist.dto;

import java.time.LocalDateTime;

/**
 * This data transfer object is used to represent user data (INCLUDING PASSWORD)
 * specifically excluding whole tasks, replaces it with amount of tasks instead.
 */
public class UserSimpleDto {

    private Long userId;
    private String username;
    private String password;
    private int taskCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserSimpleDto(Long userId, String username, String password, int taskCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.taskCount = taskCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

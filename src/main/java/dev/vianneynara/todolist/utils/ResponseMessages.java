package dev.vianneynara.todolist.utils;

import java.util.Map;

public class ResponseMessages {
    public static final Map<String, String> UNAUTHORIZED = Map.of("message", "Unauthorized");
    public static final Map<String, String> TASK_NOT_FOUND = Map.of("message", "Task not found");
    public static final Map<String, String> USER_NOT_FOUND = Map.of("message", "User not found");
    public static final Map<String, String> TASK_SUCCESSFULLY_DELETED = Map.of("message", "Task successfully deleted");
    public static final Map<String, String> TASK_SUCCESSFULLY_UPDATED = Map.of("message", "Task successfully updated");
}
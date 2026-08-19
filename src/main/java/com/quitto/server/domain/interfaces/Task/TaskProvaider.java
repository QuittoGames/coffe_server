package com.quitto.server.domain.interfaces.Task;

import java.util.HashSet;

import com.quitto.server.domain.models.Task.Task;

public interface TaskProvaider {
    void create(Task task);

    void remove(Task task);
    void remove(Long id);

    HashSet<Task> viewTasks();
}

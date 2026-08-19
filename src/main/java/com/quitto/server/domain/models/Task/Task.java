package com.quitto.server.domain.models.Task;

import java.util.LinkedList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.quitto.server.domain.enums.Task.TaskCategory;

public class Task {
    private Long id;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime expiresAt;

    private Set<TaskCategory> labels;
    private Optional<String> description;

    private List<Task> children = new LinkedList<>(); // Will have musst add adnd removess than reads beacusse this i decided usse LikedList

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Set<TaskCategory> getLabels() {
        return labels;
    }

    public void setLabels(Set<TaskCategory> labels) {
        this.labels = labels;
    }

    public void addLabels(TaskCategory label) {
        this.labels.add(label);
    }

    public Optional<String> getDescription() {
        return description;
    }

    public void changeDescription(Optional<String> description) {
        this.description = description;
    }

    public List<Task> getChildren() {
        return children;
    }

    public void setChildren(List<Task> children) {
        this.children = children;
    }

    public void addChildren(Task children) {
        this.children.add(children);
    }

    public void removeChildren(Task children) {
        this.children.remove(children);
    }

    public void removeChildren(Long id) {
        this.children.removeIf(task -> task.getId().equals(id));
    }

}

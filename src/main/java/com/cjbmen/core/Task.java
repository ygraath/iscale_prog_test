package com.cjbmen.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Task object assumes that only 8 hours a day can be processed
 *
 */
public class Task {
    public Task(Duration duration, Task... dependencies){
        this.duration = duration;

        if (dependencies != null && dependencies.length > 0) {
            for (Task task : dependencies) {
                this.dependencies.add(task);
            }
        }
    }

    public Duration getDuration() {
        return this.duration;
    }

    public List<Task> getDependencies() {
        return this.dependencies;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, duration, dependencies);
    }

    // ----- INTERNALS ----- //
    private Duration duration;
    private List<Task> dependencies = new ArrayList<>();
    private int taskId;
}

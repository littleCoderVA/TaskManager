package com.example.kyle.taskmanager;

import java.util.List;

/**
 * Created by kyle on 11/5/16.
 */

public interface TaskDaoInterface {
    public TaskInterface fetchTaskById(int taskId);
    public List<TaskInterface> fetchAllTasks();
    // add task
    public boolean addTask(TaskInterface task);
    // add list of tasks
    public boolean addTasks(List<TaskInterface> tasks);
    public boolean deleteAllTasks();
}

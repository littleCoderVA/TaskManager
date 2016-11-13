package com.example.kyle.taskmanager;

import android.database.sqlite.SQLiteDatabase;
import java.util.List;

/**
 * Created by kyle on 10/16/16.
 * Singleton ManageTaskService for Managing the tasks
 * TODO: create an interface and an serviceImpl class.
 */
final class ManageTaskService {
    private TaskDao taskRepository;
    public ManageTaskService(SQLiteDatabase db){
        taskRepository = new TaskDao(db);
    }

    /**
     * Create a task and insert it to database
     * @param taskName
     * @return task object or null if insertion failed
     */
    TaskInterface createTask(String taskName) {
        TaskInterface task = new Task(taskName);
        Integer taskId;

        // insertion failed
        // The database primary key is a signed integer, so it's safe to do int conversion here
        // autoBoxing allows us to do Integer a = i  /* i is an int*/;
        if ((taskId = (int)taskRepository.writeToDB(task)) < 0){
            return null;
        }
        task.setTaskId(taskId);
        return task;
    }

    public void recordStartTime(TaskInterface task) {
        task.startTask(System.currentTimeMillis());
    }

    public void recordEndTime(TaskInterface task) {
        task.endTask(System.currentTimeMillis());
    }

    public List<TaskInterface> retrieveAllTasks() {
        return taskRepository.fetchAllTasks();
    }
}


package com.example.kyle.taskmanager;

/**
 * Created by kyle on 10/16/16.
 * An task for task manager
 * A task must have a name, it's something after all.
 */
public class Task implements TaskInterface{
    private Integer id;
    private String name;
    private long startTime;
    private long endTime;
    private boolean finished;

    @Override
    public Integer getTaskId() {
        return id;
    }

    @Override
    public void setTaskId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean getFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Start the task upon creation
     * @param name task name
     * @param startTime created time
     */
    public Task(String name, long startTime){
        this.name = name;
        this.startTime = startTime;
        this.finished = false;
    }

    /**
     * Create a task without starting
     * @param name task name
     */
    Task(String name){
        this.name = name;
        this.finished = false;
    }
    /**
     * Create a task without properties
     */
    Task(){
        this.finished = false;
    }

    /**
     * Start task and note down the start time
     * @param startTime long
     */
    public void startTask(long startTime){
        this.startTime = startTime;
    }

    /**
     * End the activity
     * @param endTime long
     */
    public void endTask(long endTime){
        this.endTime = endTime;
        this.finished = true;
    }

    @Override
    public String toString() {
        return "\nName: " + name + "\n" + "Start time: " + startTime + "\n"
                + "End time: " + endTime + "\n" + "Finished: " + finished + "\n";
    }

}

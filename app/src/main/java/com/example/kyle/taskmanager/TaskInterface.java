package com.example.kyle.taskmanager;

/**
 * Created by kyle on 10/16/16.
 */

interface TaskInterface {
    /**
     * Start task and note down the start time
     * @param startTime long
     */
    void startTask(long startTime);

    /**
     * End the activity
     * @param endTime long
     */
    void endTask(long endTime);
    long getStartTime();
    void setStartTime(long startTime);
    long getEndTime();
    void setEndTime(long endTime);
    boolean isFinished();
    void setFinished(boolean finished);
}

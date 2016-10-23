package com.example.kyle.taskmanager;

/**
 * Created by kyle on 10/20/16.
 */

enum CommandEnum {
    /**
     * NEWTASK command will have one tailing String param for task name
     */
    NEWTASK(1, "NT", "New task"),
    STARTTASK(2, "ST", "Start task"),
    ENDTASK(3, "ET", "End task");
    private int id;
    private String abbrev;
    private String description;
    CommandEnum(int id, String abbrev, String description){
        this.id = id;
        this.abbrev = abbrev;
        this.description = description;
    }
}

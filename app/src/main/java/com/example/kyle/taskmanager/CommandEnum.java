package com.example.kyle.taskmanager;

/**
 * Created by kyle on 10/20/16.
 */

enum CommandEnum {
    /**
     * NEWTASK command will have one tailing String param for task name
     */
    NEWTASK(1, "NT", "new task"),
    STARTTASK(2, "ST", "start task"),
    ENDTASK(3, "ET", "end task");
    private int id;
    private String abbrev;
    private String description;
    CommandEnum(int id, String abbrev, String description){
        this.id = id;
        this.abbrev = abbrev;
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }
    public String getAbbrev(){
        return this.abbrev;
    }
    public int getId(){
        return this.id;
    }
}

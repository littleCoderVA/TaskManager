package com.example.kyle.taskmanager;

import android.provider.BaseColumns;

/**
 * Created by kyle on 10/23/16.
 * A good way to organize a contract class is to put definitions that are global to your whole
 * database in the root level of the class. Then create an inner class for each table that
 * enumerates its columns.
 */

public class TaskManagerContract {
    // To prevent someone from accidentally instantiating the contract class
    private TaskManagerContract() {}
    /**
     * Note: By implementing the BaseColumns interface, your inner class can inherit a primary key
     * field called _ID that some Android classes such as cursor adaptors will expect it to have.
     * It's not required, but this can help your database work harmoniously with the Android
     * framework.
     */
    /* Inner class that defines the table contents */
    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_START_TIME = "startTime";
        public static final String COLUMN_NAME_END_TIME = "endTime";
        public static final String COLUMN_NAME_FINISH = "finish";
        public static final String asterisks = "*";
    }
}

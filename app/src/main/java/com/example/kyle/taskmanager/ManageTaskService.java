package com.example.kyle.taskmanager;

import android.content.Context;

/**
 * Created by kyle on 10/16/16.
 * Singleton ManageTaskService for Managing the tasks
 */

final class ManageTaskService {
    /* Lazy loading for singleton instance, so leaving it null, didn't instantiate it here */
    private static ManageTaskService instance = null;
    private SimulatedRepositoryClass readAndWrite;

    /* Private method to ensure other classes can't invoke additional instance of this class */
    private ManageTaskService() {
        readAndWrite = SimulatedRepositoryClass.getInstance();
    }

    /* The only method outside classes can call to get the singleton instance of this class */
    static ManageTaskService getInstance(){
        if (instance == null){
            instance = new ManageTaskService();
        }
        return instance;
    }

    TaskInterface createTask(Context context, String taskName) {
        TaskInterface task = new Task(taskName);
        return readAndWrite.writeTaskToFile(context, task);
    }

    public void recordStartTime(TaskInterface task) {
        task.startTask(System.currentTimeMillis());
    }

    public void recordEndTime(TaskInterface task) {
        task.endTask(System.currentTimeMillis());
    }
}


/**
 * Singleton implementation:
 *
     An implementation of the singleton pattern must:

     ensure that only one instance of the singleton class ever exists; and
     provide global access to that instance.
     Typically, this is achieved by:

     declaring all constructors of the class to be private; and
     providing a static method that returns a reference to the instance.

 * In this example the static initializer is run when the class is initialized, after class loading but before the class is used by any thread.
    public final class Singleton {
         private static final Singleton instance = new Singleton();
         private Singleton() {}

         public static Singleton getInstance() {
         return instance;
         }
    }
 * Lazy initialization
 * A singleton implementation may use lazy initialization, where the instance is created when the static method is first invoked. If the static method may be called from multiple threads simultaneously, measures must be taken to prevent race conditions that might result in the creation of multiple instances of the class.
     public final class Singleton {
         private static Singleton instance = null;
         private Singleton() {}

         public static synchronized Singleton getInstance() {
         if (instance == null) instance = new Singleton();
         return instance;
         }
     }
 */
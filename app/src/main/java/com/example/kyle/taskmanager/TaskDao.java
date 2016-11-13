package com.example.kyle.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyle on 10/10/16.
 * Singleton class for reading and writing from file
 */

class TaskDao extends DbCURDContentProvider implements TaskDaoInterface{
    public TaskDao(SQLiteDatabase db) {
        super(db);
    }

//    /**
//     * Read results from file and have the content store in an ArrayList with each line as one element
//     * @param fileName storage file name
//     */
//    ArrayList<String> readFromFileWithLineBreaks(String fileName){
//        FileInputStream fis;
//        /**
//         * Use buffer reader to be able to read lines from fileInputStream. Otherwise
//         * fileInputStream will only have a read() method and read the whole content by byte
//         */
//        BufferedReader reader;
//        ArrayList<String> parsedContent = new ArrayList<>();
//        try {
//            fis = context.openFileInput(fileName);
//            reader = new BufferedReader(new InputStreamReader(fis));
//            String content;
//            while ((content = reader.readLine()) != null) {
//                parsedContent.add(content);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return parsedContent;
//    }
//
//    // May need a simpler version of readFromFile()
//
//    /**
//     * Save to file use openFileOutput(), get fileOutputStream back. Use getFilesDir to get the path if needed
//     * @param history record message
//     * @return true if successfully wrote into file. Vice-versa
//     */
//    boolean writeToFileWithLineBreaks(String fileName, String history){
//        FileOutputStream fos;
//        try {
//            fos = context.openFileOutput(fileName, Context.MODE_APPEND);
//            /* This is the way to get line separator */
//            fos.write(System.getProperty("line.separator").getBytes());
//            fos.write(history.getBytes());
//            fos.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    /**
//     * Save object to file
//     * @param task record message
//     */
//    TaskInterface writeTaskToFile(TaskInterface task){
//        System.out.println("Writing "+task.toString()+" to file");
//        /* if write to file succeed */
//        if (writeToFileWithLineBreaks(context.getString(R.string.data_file_name), task.toString())){
//            return task;
//        }
//        return null;
//    }
//
//    /**
//     * Insert data into the database by passing a ContentValues object to the insert() method:
//     * @param task task information
//     * @return the primary key value of the new row
//     */
//    public void writeToDB(TaskDbHelper mDbHelper, TaskInterface task){
//        /* To access the database, instantiate the subclass of SQLiteOpenHelper */
//        new AsyncWriteToDb(mDbHelper).execute(task);
//    }

    /**
     * Write task to database
     * @param task
     * @return the row ID of the newly inserted row, or -1 if an error occurred; long
     */
    public long writeToDB(TaskInterface task){
        /**
         *
         1)HashMap is a general utility class that resides in java.util.
         ContentValues on the other hand is a specific class in android.content designed to comply with Android classes like SQLiteDatabase and ContentResolver

         Note that they implement different interfaces according to aforementioned designation:
         - HashMap implements Cloneable and Serializable
         - ContentValues implements Parcelable

         2) ContentValues has a member that is HashMap with String keys:

         private HashMap<String, Object> mValues
         3) ContentValues has a number of methods to get and put typed values (like getAsFloat() etc)

         Conclusion
         You may consider ContentValues as a wrapper of HashMap to store typed values, usually along with Android SQLiteDatabase or ContentResolver.
         That's it
         */
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TaskManagerContract.TaskEntry.COLUMN_NAME_NAME, task.getName());
        values.put(TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME, task.getStartTime());
        values.put(TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME, task.getEndTime());
        values.put(TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH, task.getFinished());

        // Insert the new row, returning the primary key value of the new row
        return super.insert(TaskManagerContract.TaskEntry.TABLE_NAME, values);
    }

//    /**
//     *
//     * @param mDbHelper database helper for open db connection
//     * @param name task name
//     * @return task found by name or null if nothing found
//     */
//    public TaskInterface readFromDB(TaskDbHelper mDbHelper, String name){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                TaskManagerContract.TaskEntry._ID,
//                TaskManagerContract.TaskEntry.COLUMN_NAME_NAME,
//                TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME,
//                TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME,
//                TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH
//        };
//
//        // Filter result where "name" = {taskName}
//        String selection = TaskManagerContract.TaskEntry.COLUMN_NAME_NAME + " = ?";
//        String[] selectionArgs = {name};
//
//        // How you want the results sorted in the resulting Cursor
//        String sortOrder = TaskManagerContract.TaskEntry.TABLE_NAME + " DESC";
//
//        Cursor c = db.query(
//                TaskManagerContract.TaskEntry.TABLE_NAME, // The table to query
//                projection,                               // The columns to return
//                selection,                                // The columns for the WHERE clause
//                selectionArgs,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );

//        /**
//         To look at a row in the cursor, use one of the Cursor move methods, which you must always
//         call before you begin reading values. Generally, you should start by calling moveToFirst(),
//         which places the "read position" on the first entry in the results. For each row, you can
//         read a column's value by calling one of the Cursor get methods, such as getString() or
//         getLong(). For each of the get methods, you must pass the index position of the column you
//         desire, which you can get by calling getColumnIndex() or getColumnIndexOrThrow().
//
//         cursor.moveToFirst();
//         long itemId = cursor.getLong(
//         cursor.getColumnIndexOrThrow(FeedEntry._ID)
//         );
//
//         */
//        TaskInterface returnTask = new Task(name);
//        c.moveToFirst();
//        returnTask.setStartTime(
//                c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME))
//        );
//        returnTask.setEndTime(
//                c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME))
//        );
//        returnTask.setFinished(
//                c.getInt(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH)) != 0
//                /* false = 0; true otherwise */);
//        c.close();
//        return returnTask;
//    }

//    public void readAllTaskFromDb(Context context){
//        new AsyncReadFromDb(context).execute();
//    }

    @Override
    public TaskInterface fetchTaskById(int taskId) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TaskManagerContract.TaskEntry._ID,
                TaskManagerContract.TaskEntry.COLUMN_NAME_NAME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH
        };

        // Filter result where "name" = {taskName}
        String selection = TaskManagerContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(taskId)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = TaskManagerContract.TaskEntry.TABLE_NAME + " DESC";
        Cursor c = super.query(
                TaskManagerContract.TaskEntry.TABLE_NAME, // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        /**
         To look at a row in the cursor, use one of the Cursor move methods, which you must always
         call before you begin reading values. Generally, you should start by calling moveToFirst(),
         which places the "read position" on the first entry in the results. For each row, you can
         read a column's value by calling one of the Cursor get methods, such as getString() or
         getLong(). For each of the get methods, you must pass the index position of the column you
         desire, which you can get by calling getColumnIndex() or getColumnIndexOrThrow().

         cursor.moveToFirst();
         long itemId = cursor.getLong(
         cursor.getColumnIndexOrThrow(FeedEntry._ID)
         );

         */
        TaskInterface returnTask = new Task();
        c.moveToFirst();
        returnTask.setTaskId(c.getInt(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry._ID)));
        returnTask.setStartTime(
                c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME))
        );
        returnTask.setEndTime(
                c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME))
        );
        returnTask.setFinished(
                c.getInt(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH)) != 0
                /* false = 0; true otherwise */);
        c.close();
        return returnTask;
    }

    @Override
    public List<TaskInterface> fetchAllTasks() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TaskManagerContract.TaskEntry._ID,
                TaskManagerContract.TaskEntry.COLUMN_NAME_NAME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME,
                TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH
        };

        // Filter result where "name" = {taskName}
//        String selection = TaskManagerContract.TaskEntry.COLUMN_NAME_NAME + " = ?";
//        String[] selectionArgs = {"*"};

        // How you want the results sorted in the resulting Cursor
        Cursor c = super.query(
                TaskManagerContract.TaskEntry.TABLE_NAME, // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        /**
         To look at a row in the cursor, use one of the Cursor move methods, which you must always
         call before you begin reading values. Generally, you should start by calling moveToFirst(),
         which places the "read position" on the first entry in the results. For each row, you can
         read a column's value by calling one of the Cursor get methods, such as getString() or
         getLong(). For each of the get methods, you must pass the index position of the column you
         desire, which you can get by calling getColumnIndex() or getColumnIndexOrThrow().

         cursor.moveToFirst();
         long itemId = cursor.getLong(
         cursor.getColumnIndexOrThrow(FeedEntry._ID)
         );

         */
        List<TaskInterface> tasks = new ArrayList<>();
        c.moveToFirst();
        int end = c.getCount();
        while (c.getPosition() < end) {
            TaskInterface tempTask = new Task();
            tempTask.setTaskId(c.getInt(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry._ID)));
            tempTask.setName(c.getString(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_NAME)));
            tempTask.setStartTime(
                    c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME))
            );
            tempTask.setEndTime(
                    c.getLong(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME))
            );
            tempTask.setFinished(
                    c.getInt(c.getColumnIndexOrThrow(TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH)) != 0
                /* false = 0; true otherwise */);
            tasks.add(tempTask);
            c.moveToNext();
        }
        c.close();
        return tasks;
    }

    @Override
    public boolean addTask(TaskInterface task) {
        return false;
    }

    @Override
    public boolean addTasks(List<TaskInterface> tasks) {
        return false;
    }

    @Override
    public boolean deleteAllTasks() {
        return false;
    }

//    /**
//     * Integer is used for onProgress update
//     * Boolean is used for doInBackground as well as param for onPostExecute
//     */
//    private class AsyncReadFromDb extends AsyncTask<String, Integer, List<TaskInterface>> {
//        TaskDbHelper dbHelper;
//        Context context;
//        AsyncReadFromDb(Context context){
//            this.context = context;
//            this.dbHelper = TaskDbHelper.getInstance(context);
//        }
//
//        /**
//         * Override this method to perform a computation on a background thread. The
//         * specified parameters are the parameters passed to {@link #execute}
//         * by the caller of this task.
//         * <p>
//         * This method can call {@link #publishProgress} to publish updates
//         * on the UI thread.
//         *
//         * @param params The parameters of the task.
//         * @return A result, defined by the subclass of this task.
//         * @see #onPreExecute()
//         * @see #onPostExecute
//         * @see #publishProgress
//         */
//        @Override
//        protected List<TaskInterface> doInBackground(String... params) {
//            SQLiteDatabase db = dbHelper.getReadableDatabase();
//                return queryAllFromDb(db);
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
////            setProgressPercent(progress[0]);
//        }
//        @Override
//        protected void onPostExecute(List<TaskInterface> result) {
//            if (result != null){
//                Toast.makeText(context, "db call works", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    /**
//     *
//     */
//    private class AsyncWriteToDb extends AsyncTask<TaskInterface, Integer, TaskInterface> {
//        TaskDbHelper dbHelper;
//        SQLiteDatabase db;
//        AsyncWriteToDb(TaskDbHelper dbHelper){
//            this.dbHelper = dbHelper;
//        }
//        @Override
//        protected TaskInterface doInBackground(TaskInterface... params) {
//            db = dbHelper.getWritableDatabase();
//            return params[0];
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... progress) {
////            setProgressPercent(progress[0]);
//        }
//        @Override
//        protected void onPostExecute(TaskInterface task) {
//            queryWriteToDB(db, task);
////            showDialog("Downloaded " + result + " bytes");
//        }
//    }
}

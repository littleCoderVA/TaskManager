package com.example.kyle.taskmanager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Instantiate DatabaseHelper, open and close database; Instantiate Dao object and public it for
 * caller to operate on.
 * Created by kyle on 11/12/16.
 */
public class Database {
    private TaskDbHelper mDbHelper;
    public static TaskDaoInterface taskDao;

    public void open(Context context) throws SQLException {
        // getWritable database is a timely operation so run it with asyncTask
        new DataBaseOpen(context).execute();
    }

    public void close(Context context) {
        mDbHelper = TaskDbHelper.getInstance(context);
        mDbHelper.close();
    }
    /**
     * Integer is used for onProgress update
     * Boolean is used for doInBackground as well as param for onPostExecute
     */
    private class DataBaseOpen extends AsyncTask<String, Integer, SQLiteDatabase> {
        TaskDbHelper dbHelper;
        Context context;

        DataBaseOpen(Context context) {
            this.context = context;
            this.dbHelper = TaskDbHelper.getInstance(context);
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected SQLiteDatabase doInBackground(String... params) {
            // get writableDatabase if we want to read and write to db. But get readableDatabase if
            // we only need to fetch data
            return dbHelper.getWritableDatabase();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(SQLiteDatabase result) {
            if (result != null) {
                Toast.makeText(context, "db call works", Toast.LENGTH_SHORT).show();
                taskDao = new TaskDao(result);
                ((MainActivity)context).dbOpenedCallback(result);
            } else {
                Toast.makeText(context, "db call failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

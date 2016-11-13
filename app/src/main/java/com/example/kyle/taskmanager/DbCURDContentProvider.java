package com.example.kyle.taskmanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by kyle on 11/5/16.
 * Manage CURD database operation
 */

public abstract class DbCURDContentProvider {
    private SQLiteDatabase db;

    public DbCURDContentProvider(SQLiteDatabase db) {
        this.db = db;
    }
    public long insert(String tableName, ContentValues values){
        /**
         * The first argument for insert() is simply the table name.
         * The second argument tells the framework what to do in the event that the ContentValues is
         * empty (i.e., you did not put any values). If you specify the name of a column, the
         * framework inserts a row and sets the value of that column to null. If you specify null,
         * like in this code sample, the framework does not insert a row when there are no values.
         */
        return db.insert(tableName, null, values);
    }
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy){
        return db.query(
                table,                                 // The table to query
                columns,                               // The columns to return
                selection,                             // The columns for the WHERE clause
                selectionArgs,                         // The values for the WHERE clause
                groupBy,                               // don't group the rows
                having,                                // don't filter by row groups
                orderBy                                // The sort order
        );
    }
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        return db.update(table, values, whereClause, whereArgs);
    }
    // delete task
    public int delete(String table, String whereClause, String[] whereArgs){
        return db.delete(table, whereClause, whereArgs);
    }
}

package com.example.kyle.taskmanager;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by kyle on 10/23/16.
 * A helper class to manage database creation and version management.
 *
 * Long notes from stackOverflow:
 The SqliteOpenHelper object holds on to one database connection. It appears to offer you a read and
 write connection, but it really doesn't. Call the read-only, and you'll get the write database
 connection regardless.
 So, one helper instance, one db connection. Even if you use it from multiple threads, one
 connection at a time. The SqliteDatabase object uses java locks to keep access serialized. So, if
 100 threads have one db instance, calls to the actual on-disk database are serialized.
 So, one helper, one db connection, which is serialized in java code. One thread, 1000 threads, if
 you use one helper instance shared between them, all of your db access code is serial. And life is
 good (ish).
 If you try to write to the database from actual distinct connections at the same time, one will
 fail. It will not wait till the first is done and then write. It will simply not write your change.
 Worse, if you don’t call the right version of insert/update on the SQLiteDatabase, you won’t get an
 exception. You’ll just get a message in your LogCat, and that will be it.
 So, multiple threads? Use one helper. Period. If you KNOW only one thread will be writing, you MAY
 be able to use multiple connections, and your reads will be faster, but buyer beware. I haven't
 tested that much.

 * Therefore, making this a singleton as we should only have one connection to db so all our db
 * actions will use the same helper and each thread will wait for others.
 */
public class TaskDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    /**
     * DbHelper manages the static constants for database, while TaskManagerContract manages static
     * constants for each tables
     */
    public static final String DATABASE_NAME = "TaskManager.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskManagerContract.TaskEntry.TABLE_NAME + " (" +
                    TaskManagerContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskManagerContract.TaskEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    TaskManagerContract.TaskEntry.COLUMN_NAME_START_TIME + INTEGER_TYPE + COMMA_SEP +
                    TaskManagerContract.TaskEntry.COLUMN_NAME_END_TIME + INTEGER_TYPE + COMMA_SEP +
                    TaskManagerContract.TaskEntry.COLUMN_NAME_FINISH + BOOLEAN_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskManagerContract.TaskEntry.TABLE_NAME;

//    /**
//     * Create a helper object to create, open, and/or manage a database.
//     *
//     * @param context      to use to open or create the database
//     * @param name         of the database file, or null for an in-memory database
//     * @param factory      to use for creating cursor objects, or null for the default
//     * @param version      number of the database (starting at 1); if the database is older,
//     *                     {@link #onUpgrade} will be used to upgrade the database; if the database is
//     *                     newer, {@link #onDowngrade} will be used to downgrade the database
//     * @param errorHandler the {@link DatabaseErrorHandler} to be used when sqlite reports database
//     */
//    public TaskDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
// int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }
    private static TaskDbHelper instance = null;
    public static synchronized TaskDbHelper getInstance(Context context){
        if (instance == null){
            instance = new TaskDbHelper(context);
        }
        return instance;
    }
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    private TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* For now our upgrade will create a new table with version appended to the db name.
        We will do data migration and remove the old table manually later */
//        db.execSQL(SQL_DELETE_ENTRIES);
        Log.w(TAG, "Upgrading database from version "
        + oldVersion + " to "
                + newVersion + " which destroys all old data");
        db.execSQL("DROP TABLE IF EXISTS "
                + TaskManagerContract.TaskEntry.TABLE_NAME);
        onCreate(db);
        onCreate(db);
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

/*
    Notes for primary keys and row id
    ROWIDs and the INTEGER PRIMARY KEY

Except for WITHOUT ROWID tables, all rows within SQLite tables have a 64-bit signed integer key that uniquely identifies the row within its table. This integer is usually called the "rowid". The rowid value can be accessed using one of the special case-independent names "rowid", "oid", or "_rowid_" in place of a column name. If a table contains a user defined column named "rowid", "oid" or "_rowid_", then that name always refers the explicitly declared column and cannot be used to retrieve the integer rowid value.

The rowid (and "oid" and "_rowid_") is omitted in WITHOUT ROWID tables. WITHOUT ROWID tables are only available in SQLite version 3.8.2 (2013-12-06) and later. A table that lacks the WITHOUT ROWID clause is called a "rowid table".

The data for rowid tables is stored as a B-Tree structure containing one entry for each table row, using the rowid value as the key. This means that retrieving or sorting records by rowid is fast. Searching for a record with a specific rowid, or for all records with rowids within a specified range is around twice as fast as a similar search made by specifying any other PRIMARY KEY or indexed value.

With one exception noted below, if a rowid table has a primary key that consists of a single column and the declared type of that column is "INTEGER" in any mixture of upper and lower case, then the column becomes an alias for the rowid. Such a column is usually referred to as an "integer primary key". A PRIMARY KEY column only becomes an integer primary key if the declared type name is exactly "INTEGER". Other integer type names like "INT" or "BIGINT" or "SHORT INTEGER" or "UNSIGNED INTEGER" causes the primary key column to behave as an ordinary table column with integer affinity and a unique index, not as an alias for the rowid.

The exception mentioned above is that if the declaration of a column with declared type "INTEGER" includes an "PRIMARY KEY DESC" clause, it does not become an alias for the rowid and is not classified as an integer primary key. This quirk is not by design. It is due to a bug in early versions of SQLite. But fixing the bug could result in backwards incompatibilities. Hence, the original behavior has been retained (and documented) because odd behavior in a corner case is far better than a compatibility break. This means that the following three table declarations all cause the column "x" to be an alias for the rowid (an integer primary key):

CREATE TABLE t(x INTEGER PRIMARY KEY ASC, y, z);
CREATE TABLE t(x INTEGER, y, z, PRIMARY KEY(x ASC));
CREATE TABLE t(x INTEGER, y, z, PRIMARY KEY(x DESC));
But the following declaration does not result in "x" being an alias for the rowid:

CREATE TABLE t(x INTEGER PRIMARY KEY DESC, y, z);
Rowid values may be modified using an UPDATE statement in the same way as any other column value can, either using one of the built-in aliases ("rowid", "oid" or "_rowid_") or by using an alias created by an integer primary key. Similarly, an INSERT statement may provide a value to use as the rowid for each row inserted. Unlike normal SQLite columns, an integer primary key or rowid column must contain integer values. Integer primary key or rowid columns are not able to hold floating point values, strings, BLOBs, or NULLs.

If an UPDATE statement attempts to set an integer primary key or rowid column to a NULL or blob value, or to a string or real value that cannot be losslessly converted to an integer, a "datatype mismatch" error occurs and the statement is aborted. If an INSERT statement attempts to insert a blob value, or a string or real value that cannot be losslessly converted to an integer into an integer primary key or rowid column, a "datatype mismatch" error occurs and the statement is aborted.

If an INSERT statement attempts to insert a NULL value into a rowid or integer primary key column, the system chooses an integer value to use as the rowid automatically. A detailed description of how this is done is provided separately.

The parent key of a foreign key constraint is not allowed to use the rowid. The parent key must used named columns only.
 */
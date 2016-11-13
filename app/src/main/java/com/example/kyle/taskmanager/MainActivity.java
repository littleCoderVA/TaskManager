package com.example.kyle.taskmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private static Database database;
    private Fragment dialog;
    private LinearLayout linearLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ManageTaskService manageTaskService;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // first open database
        database = new Database();
        database.open(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabListener();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Check if user has given permission to record audio
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
        if (savedInstanceState == null) {
            dialog = new PopupDialogFragment();
        }
        linearLayout = (LinearLayout) findViewById(R.id.record_history);
        status = (TextView) findViewById(R.id.recording_status);
        status.setBackgroundColor(Color.RED);
    }

    public void saveRecordingHistory(String text) {
        TextView record = new TextView(MainActivity.this);
        record.setText(text);
        record.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(record);
        saveHistoryToFile(text);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Gets called after user clicks confirm on voice recognition confirmation popup
     */
    public void popUpAccept(String commandString) {

        //ArrayList<String> tokenizeCommand = commandTokenizer(commandString);

        //if (tokenizeCommand != null) {
        String commandDesc;
        if (commandString.contains((commandDesc = CommandEnum.NEWTASK.getDescription()))) {
            TaskInterface task;
            String arguments = commandString.replace(commandDesc, "");
            if ((task = manageTaskService.createTask(arguments)) != null) {
                linearLayout.addView(createNewTextViewRecord(task.toString()));
            }
        }
        else if(commandString.contains((CommandEnum.STARTTASK.getDescription()))) {
            //command = CommandEnum.STARTTASK;
        }
        else if(commandString.contains((CommandEnum.ENDTASK.getDescription()))) {
            //command = CommandEnum.ENDTASK;
        }

    }
//
//    /**
//     * Process the raw input string into recognizable command with/without additional parameters
//     *
//     * @param commandString raw input string with spaces
//     * @return ArrayList of string token with format of {[command][parameter 1]...[parameter n]}
//     */
//    private ArrayList<String> commandTokenizer(String commandString) {
//        // TODO: need to make this method more powerful to process raw string inputs, otherwise the
//        // trade of for creating these new ArrayList are too much
//        String matchingEnumDesc;
//        if (commandString.contains((matchingEnumDesc=CommandEnum.NEWTASK.getDescription()))) {
//            ArrayList<String> tokenizeCommand = new ArrayList<>();
//            /* Command */
//            tokenizeCommand.add(Integer.toString(CommandEnum.NEWTASK.getId()));
//            /* Additional parameter */
//            tokenizeCommand.add(commandString.replace(matchingEnumDesc, ""));
//            return tokenizeCommand;
//        }
//        Log.e("Error spotted", "commandTokenizer");
//        return null;
//    }

    /**
     * Gets called after user clicks decline on voice recognition confirmation popup
     */
    public void popUpDecline() {
        startSpeechRecognizer();
    }

    /**
     * Gets called after user clicks decline on voice recognition confirmation popup
     */
    public void popUpNeutral() {
        startSpeechRecognizer();
    }

    public void startSpeechRecognizer() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        /*
        * EXTRA_LANGUAGE_MODEL:
        * Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH.
         * The recognizer uses this information to fine tune the results. This extra is required.
         * Activities implementing ACTION_RECOGNIZE_SPEECH may interpret the values as they see fit.
        * */
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // accept partial results if they come
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        startActivityForResult(recognizerIntent, REQ_CODE_SPEECH_INPUT);
    }

    private void fabListener() {
        startSpeechRecognizer();
        // Using MainActivity.this as an attempt to prevent unnecessary overhead
        Toast.makeText(MainActivity.this, "Fab pressed, start listening", Toast.LENGTH_SHORT).show();
    }

    /**
     * Save record to file
     *
     * @param history
     */
    private void saveHistoryToFile(String history) {
//        manageTaskService(MainActivity.this.getString(R.string.data_file_name), history);
    }

    public void dbOpenedCallback(SQLiteDatabase database){
        MainActivity.this.instantiateManageTaskService(database);
        MainActivity.this.showDbReadyColor();
        MainActivity.this.loadHistoryFromFile();
    }
    /**
     * Read record from file then feed the results into linearLayout
     */
    private void loadHistoryFromFile() {
        List<TaskInterface> contents = manageTaskService.retrieveAllTasks();
        for (TaskInterface task : contents) {
            linearLayout.addView(createNewTextViewRecord(task.toString()));
        }
    }

    private void showDbReadyColor() {
        status.setBackgroundColor(Color.TRANSPARENT);
    }

    private void instantiateManageTaskService(SQLiteDatabase db) {
        if (manageTaskService == null) {
            manageTaskService = new ManageTaskService(db);
        }
    }

    private TextView createNewTextViewRecord(String content) {
        TextView record = new TextView(MainActivity.this);
        record.setText(content);
        record.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        return record;
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    /* data contains a list of possible results. First one being the closest. */
                    String speechResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    Bundle resultPassToPopUp = new Bundle();
                    resultPassToPopUp.putString(
                            MainActivity.this.getString(R.string.bundle_tag_recording_result),
                            speechResult);
                    // prompt the user if that's the right result, if yes proceed with action.
                    // Otherwise user can just click the fab button and record again
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.setArguments(resultPassToPopUp);
                    ft.add(dialog, MainActivity.this.getString(R.string.dialog_fragment)).commit();
                }
                break;
            }
        }
    }
}

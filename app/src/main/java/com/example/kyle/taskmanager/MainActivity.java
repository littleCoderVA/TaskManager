package com.example.kyle.taskmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Fragment dialog;
    private LinearLayout linearLayout;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
        linearLayout = (LinearLayout)findViewById(R.id.record_history);
        // Populate the listview with previous records
        loadHistoryFromFile();
    }
    public void saveRecordingHistory(String text){
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
    public void startSpeechRecognizer(){
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
    private void fabListener(){
        startSpeechRecognizer();
        // Using MainActivity.this as an attempt to prevent unnecessary overhead
        Toast.makeText(MainActivity.this, "Fab pressed, start listening", Toast.LENGTH_SHORT).show();
    }

    /**
     * Save to file use openFileOutput(), get fileOutputStream back. Use getFilesDir to get the path
     * @param history
     */
    private void saveHistoryToFile(String history){
        String FILENAME = MainActivity.this.getString(R.string.data_file_name);
        FileOutputStream fos;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            /* This is the way to get line separator */
            fos.write(System.getProperty("line.separator").getBytes());
            fos.write(history.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read from file use openFileInput, and read() will return byte of data.
     */
    private void loadHistoryFromFile(){
        String FILENAME = MainActivity.this.getString(R.string.data_file_name);
        FileInputStream fis;
        /**
         * Use buffer reader to be able to read lines from fileInputStream. Otherwise
         * fileInputStream will only have a read() method and read the whole content by byte
         */
        BufferedReader reader;
        try {
            fis = openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(fis));
            String content;
            TextView record;
            while ((content = reader.readLine()) != null) {
                record = new TextView(MainActivity.this);
                record.setText(content);
                record.setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.addView(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

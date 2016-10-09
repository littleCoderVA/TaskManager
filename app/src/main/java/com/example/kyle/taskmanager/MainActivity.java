package com.example.kyle.taskmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private SpeechRecognizer recognizer;
    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Fragment dialog;
    private LinearLayout linearLayout;

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
        recognizer = setUpSpeechRecognizer();
        if (savedInstanceState == null) {
            dialog = new PopupDialogFragment();
        }
        linearLayout = (LinearLayout)findViewById(R.id.record_history);
        TextView record = new TextView(MainActivity.this);
        record.setText("test");
        record.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(record);
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
    private SpeechRecognizer setUpSpeechRecognizer(){
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new listener());
        return speechRecognizer;
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
        recognizer.startListening(recognizerIntent);
    }
    private void fabListener(){
        startSpeechRecognizer();
        // Using MainActivity.this as an attempt to prevent unnecessary overhead
        Toast.makeText(MainActivity.this, "Fab pressed, start listening", Toast.LENGTH_SHORT).show();
    }
    private void loadHistoryFromFile(){

    }
    private void saveHistoryToFile(String history){
        String FILENAME = "hello_file";

        FileOutputStream fos;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_APPEND);
            System.out.println(getFilesDir().getAbsolutePath());
            System.out.println(getFileStreamPath("hello_file"));
            fos.write(history.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * Implement the recognitionListener as inner class allows it to access fields inside activity class
     */
    class listener implements RecognitionListener {
        private String[] errorCodes = getResources().getStringArray(R.array.ErrorCodeMapping);

        /**
         * Called when the endpointer is ready for the user to start speaking.
         *
         * @param params parameters set by the recognition service. Reserved for future use.
         */
        @Override
        public void onReadyForSpeech(Bundle params) {
            ((TextView)findViewById(R.id.recording_status)).setText("Ready for speech");
        }

        /**
         * The user has started to speak.
         */
        @Override
        public void onBeginningOfSpeech() {
            TextView status = ((TextView)findViewById(R.id.recording_status));
            status.setText(R.string.speech_began);
            status.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_green_light));
        }

        /**
         * The sound level in the audio stream has changed. There is no guarantee that this method will
         * be called.
         *
         * @param rmsdB the new RMS dB value
         */
        @Override
        public void onRmsChanged(float rmsdB) {
            // R.string.volume use %.6f as a place holder, and pass that float value with 6 decimal
            // points into the string. getString is the same as MainActivity.this.getString()
            ((TextView)findViewById(R.id.volume_status)).setText(getString(R.string.volume, rmsdB));
        }

        /**
         * More sound has been received. The purpose of this function is to allow giving feedback to the
         * user regarding the captured audio. There is no guarantee that this method will be called.
         *
         * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
         *               single channel audio stream. The sample rate is implementation dependent.
         */
        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        /**
         * Called after the user stops speaking.
         */
        @Override
        public void onEndOfSpeech() {
            TextView status = (TextView)findViewById(R.id.recording_status);
            status.setText(R.string.speech_ended);
            status.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_light));

        }

        /**
         * A network or recognition error occurred.
         *
         * @param error code is defined in {@link SpeechRecognizer}
         */
        @Override
        public void onError(int error) {
            TextView status = ((TextView)findViewById(R.id.recording_status));
            status.setText(errorCodes[error]);
            status.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.holo_red_light));
        }

        /**
         * Called when recognition results are ready.
         *
         * @param results the recognition results. To retrieve the results in {@code
         *                ArrayList<String>} format use {@link Bundle#getStringArrayList(String)} with
         *                {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter. A float array of
         *                confidence values might also be given in {@link SpeechRecognizer#CONFIDENCE_SCORES}.
         */
        @Override
        public void onResults(Bundle results) {
            // An array of possible recognition with the first one being the most accurate.
            ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (data != null && data.size() >= 0) {
                ((TextView) findViewById(R.id.recording_status)).setText(data.get(0).toString());

                // prompt the user if that's the right result, if yes proceed with action. Otherwise user can just click the fab button and record again
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.setArguments(results);
                ft.add(dialog, MainActivity.this.getString(R.string.dialog_fragment)).commit();
            }
        }

        /**
         * Called when partial recognition results are available. The callback might be called at any
         * time between {@link #onBeginningOfSpeech()} and {@link #onResults(Bundle)} when partial
         * results are ready. This method may be called zero, one or multiple times for each call to
         * {@link SpeechRecognizer#startListening(Intent)}, depending on the speech recognition
         * service implementation.  To request partial results, use
         * {@link RecognizerIntent#EXTRA_PARTIAL_RESULTS}
         *
         * @param partialResults the returned results. To retrieve the results in
         *                       ArrayList&lt;String&gt; format use {@link Bundle#getStringArrayList(String)} with
         *                       {@link SpeechRecognizer#RESULTS_RECOGNITION} as a parameter
         */
        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        /**
         * Reserved for adding future events.ss
         *
         * @param eventType the type of the occurred event
         * @param params    a Bundle containing the passed parameters
         */
        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}

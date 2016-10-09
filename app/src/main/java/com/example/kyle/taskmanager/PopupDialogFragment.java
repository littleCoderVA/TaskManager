package com.example.kyle.taskmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kyle on 10/1/16.
 * Speech recognition sometimes is not always accurate. Using a pop-up dialog for user to confirm
 * the recognition result.
 */

public class PopupDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.check_recognition)
                .setPositiveButton(R.string.match_correct, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "matched", Toast.LENGTH_SHORT).show();
                        // somehow the saveInstanceState are always null, using getArguments helps
                        Bundle bundle = getArguments();
                        ArrayList<String> result;
                        if (bundle != null && (result = bundle
                                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION))!=null) {
                            ((MainActivity) getActivity()).saveRecordingHistory(result.get(0));
                        }
                    }
                })
                .setNegativeButton(R.string.match_incorrect, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        // can't call MainActivity.something because that's the static reference
                        ((MainActivity)getActivity()).startSpeechRecognizer();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
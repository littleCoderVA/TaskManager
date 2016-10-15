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
        // somehow the saveInstanceState are always null, using getArguments helps
        Bundle bundle = getArguments();
        String result = null;
        if (bundle != null && (result = bundle
                .getString(getActivity().getString(R.string.bundle_tag_recording_result)))!=null) {
            ((MainActivity) getActivity()).saveRecordingHistory(result);
        } else {
            Toast.makeText(getActivity(), "Bundle is null or value not found", Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.check_recognition, result))
                .setPositiveButton(R.string.match_correct, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "matched", Toast.LENGTH_SHORT).show();
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

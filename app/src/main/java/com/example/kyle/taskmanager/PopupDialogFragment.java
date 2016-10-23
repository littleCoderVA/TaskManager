package com.example.kyle.taskmanager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by kyle on 10/1/16.
 * Speech recognition sometimes is not always accurate. Using a pop-up dialog for user to confirm
 * the recognition result.
 */

public class PopupDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        /* this result will be used in the inter onClickListener class, needs to be final */
        final String result = getResultFromBundle();
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (result != null) {
            builder.setMessage(getActivity().getString(R.string.check_recognition, result))
                    .setPositiveButton(R.string.match_correct, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(getActivity(), "matched", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).popUpAccept(result);
                        }
                    })
                    .setNegativeButton(R.string.match_incorrect, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            // can't call MainActivity.something because that's a static reference
                            ((MainActivity) getActivity()).popUpDecline();
                        }
                    });
        } else {
            /* This shouldn't happen*/
            builder.setMessage("Bundle is null or value not found, call Kyle for help")
                    .setNeutralButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ((MainActivity) getActivity()).popUpNeutral();
                        }
                    });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * Get the string result from bundle. Null if bundle is not null or value not found.
     * @return result from bundle
     */
    private String getResultFromBundle(){
        // somehow the saveInstanceState are always null, using getArguments helps
        Bundle bundle = getArguments();
        /* using tempResult variable to prevent additional calls to activity to get result */
        String tempResult;
        if (bundle != null && (tempResult = bundle
                .getString(getActivity().getString(R.string.bundle_tag_recording_result)))!=null) {
            return tempResult;
        } else {
//            Toast.makeText(getActivity(), "Bundle is null or value not found", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}

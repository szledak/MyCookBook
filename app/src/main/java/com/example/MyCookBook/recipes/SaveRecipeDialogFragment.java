package com.example.MyCookBook.recipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import info.androidhive.actionbar.R;

/**
 * Created by Sonia on 2015-05-28.
 */
public class SaveRecipeDialogFragment extends DialogFragment {
    public static final String ARG_ITEM_ID = "save_dialog_fragment";

    public interface SaveRecipeDialogFragmentListener {
        void onFinishDialog();
    }

    public SaveRecipeDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setMessage("Zapisać przepis do pliku?")
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SaveRecipeDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

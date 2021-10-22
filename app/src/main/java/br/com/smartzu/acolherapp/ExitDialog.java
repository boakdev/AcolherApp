package br.com.smartzu.acolherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ExitDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private ExitListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ExitListener)) {
            throw new RuntimeException("A activity precisa implementar a a interface ExitDialog.ExitListener");
        }
        listener = (ExitListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Tem certeza que deseja sair?")
                .setPositiveButton("SIM", this)
                .setNegativeButton("NÃO", this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE && listener == null) {
            listener.onExit();
        }
    }

    public interface ExitListener {
        public void onExit();
    }
}

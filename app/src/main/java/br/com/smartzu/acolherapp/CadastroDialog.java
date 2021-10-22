package br.com.smartzu.acolherapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class CadastroDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private CadastroListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof CadastroListener)) {
            throw new RuntimeException("A activity precisa implementar a a interface CadastroDialog.CadastroListener");
        }
        listener = (CadastroListener) activity;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Cadastro Visitante")
                .setIcon(android.R.drawable.ic_menu_send)
                .setMessage("Você confirma o cadastro do visitante?")

                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "retorno SIM", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "retorno NÃO", Toast.LENGTH_LONG).show();
                    }
                })
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == DialogInterface.BUTTON_POSITIVE && listener == null) {
            listener.onCadastro();
        }
    }

    public interface CadastroListener {
        public void onCadastro();
    }
}

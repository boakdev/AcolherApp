package br.com.smartzu.acolherapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, 16, 30, true);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        String hora = String.format("Você escolheu o horário de %02d:%02d", hourOfDay, minute);
        Toast.makeText(getActivity(), hora, Toast.LENGTH_LONG).show();
    }
}

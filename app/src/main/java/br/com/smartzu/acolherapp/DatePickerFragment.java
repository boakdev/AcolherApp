package br.com.smartzu.acolherapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String data;
    private String dataChecked;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        data = String.format("Você escolheu a data %02d/%02d/%d", dayOfMonth, month + 1, year);
        dataChecked = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
        Toast.makeText(getActivity(), dataChecked, Toast.LENGTH_LONG).show();
    }
}

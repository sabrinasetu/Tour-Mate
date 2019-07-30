package com.example.maruf.tourMateApplication.OtherClass;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePicker {
    public void datePicker(final EditText editText, final Context context){
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String myformat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myformat,Locale.US);
                editText.setText(sdf.format(calendar.getTime()));
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatePickerDialog  datePickerDialog=new DatePickerDialog(context,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
               datePickerDialog.setCanceledOnTouchOutside(true);
               datePickerDialog.show();
            }
        });



    }
}

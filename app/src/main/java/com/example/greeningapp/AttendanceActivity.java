package com.example.greeningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class AttendanceActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btn_attendcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.calendarView);
        btn_attendcheck = findViewById(R.id.btn_attendcheck);

        // Set the calendar date change listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Show the attendance button when a date is selected
                btn_attendcheck.setVisibility(View.VISIBLE);
            }
        });

        // Set the button click listener
        btn_attendcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a popup message for attendance completion
                Toast.makeText(AttendanceActivity.this, "출석체크 완료", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
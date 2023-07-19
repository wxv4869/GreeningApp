package com.example.greeningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AttendanceActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btn_attendcheck;
    private DatabaseReference attendanceRef; // Realtime Database 참조
    private String idToken; // 사용자 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.calendarView);
        btn_attendcheck = findViewById(R.id.btn_attendcheck);
        attendanceRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child("User").child(idToken).child("attendance");

        Intent intent = getIntent();
        if (intent != null) {
            idToken = intent.getStringExtra("idToken");
            if (idToken != null) {
                attendanceRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child("User").child(idToken).child("attendance");
            }
        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                // Uncaught exception occurred, print the stack trace to identify the issue
                throwable.printStackTrace();
                Toast.makeText(AttendanceActivity.this, "An error occurred, please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the calendar date change listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Show the attendance button when a date is selected
                btn_attendcheck.setVisibility(View.VISIBLE);

                // Check if attendance is already completed for the selected date
                String selectedDate = formatDate(year, month, dayOfMonth);
                attendanceRef.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted) {
                            // If attendance is already completed, disable the button
                            btn_attendcheck.setEnabled(false);
                        } else {
                            // If attendance is not completed, enable the button
                            btn_attendcheck.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });
            }
        });

        // Set the button click listener
        btn_attendcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected date from the calendar
                long selectedDateInMillis = calendarView.getDate();
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTimeInMillis(selectedDateInMillis);
                int year = selectedCalendar.get(Calendar.YEAR);
                int month = selectedCalendar.get(Calendar.MONTH);
                int dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                String selectedDate = formatDate(year, month, dayOfMonth);

                // Check if attendance is already completed for the selected date
                attendanceRef.child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted) {
                            // If attendance is already completed, show a message
                            Toast.makeText(AttendanceActivity.this, "이미 출석체크를 완료했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // If attendance is not completed, mark it as completed and show a success message
                            markAttendanceCompletedForDate(selectedDate);
                            Toast.makeText(AttendanceActivity.this, "출석체크 완료", Toast.LENGTH_SHORT).show();
                            // Disable the button after completing attendance
                            btn_attendcheck.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle database error
                    }
                });
            }
        });
    }

    // Format the date as "yyyy-MM-dd"
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    // Mark attendance as completed for the given date
    private void markAttendanceCompletedForDate(String date) {
        attendanceRef.child(date).setValue(true);
    }
}
package com.example.greeningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference userRef; // Realtime Database 참조
    private String idToken; // 사용자 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.calendarView);
        btn_attendcheck = findViewById(R.id.btn_attendcheck);

        // 파이어베이스에서 현재 로그인된 사용자의 데이터 참조
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            idToken = firebaseUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("UserAccount").child(idToken);
        } else {
            // 로그인 정보가 없으면 로그인 화면으로 이동하거나 적절히 처리해야 합니다.
            Toast.makeText(this, "로그인 정보를 찾을 수 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set the calendar date change listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Show the attendance button when a date is selected
                btn_attendcheck.setVisibility(View.VISIBLE);

                // Check if attendance is already completed for the selected date
                String selectedDate = formatDate(year, month, dayOfMonth);
                userRef.child("attendance").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted != null && attendanceCompleted) {
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
                userRef.child("attendance").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted != null && attendanceCompleted) {
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
        userRef.child("attendance").child(date).setValue(true);
    }
}
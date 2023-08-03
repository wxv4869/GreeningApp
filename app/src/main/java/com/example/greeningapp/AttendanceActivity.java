package com.example.greeningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
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
    private TextView attendanceCompletedTextView;
    private Button btn_attendcheck;
    private Button btn_home;
    private DatabaseReference userRef; // Firebase Realtime Database 참조
    private String idToken; // 사용자 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        calendarView = findViewById(R.id.calendarView);
        attendanceCompletedTextView = findViewById(R.id.tv_attendance_completed);
        btn_attendcheck = findViewById(R.id.btn_attendcheck);
        btn_home = findViewById(R.id.btn_home);

        // 홈으로 이동하기 버튼 클릭 시 호출되는 리스너 설정
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity로 이동하는 Intent 생성 (임시로 ProductListActivity로 이동하도록 설정함)
                Intent intent = new Intent(AttendanceActivity.this, ProductListActivity.class);
                startActivity(intent);
                finish(); // AttendanceActivity는 더 이상 필요하지 않으므로 종료함
            }
        });

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

        // 캘린더뷰에서 날짜를 선택할 때마다 호출되는 리스너 설정
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // 출석체크 버튼을 보이게 설정
                btn_attendcheck.setVisibility(View.VISIBLE);

                // 출석체크 완료 텍스트뷰는 보이게, 캘린더뷰는 안 보이게 설정
                calendarView.setVisibility(View.VISIBLE);
                attendanceCompletedTextView.setVisibility(View.INVISIBLE);

                // 선택된 날짜를 문자열로 변환하여 Firebase에서 해당 날짜의 출석체크 데이터 여부를 확인
                String selectedDate = formatDate(year, month, dayOfMonth);
                userRef.child("attendance").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted != null && attendanceCompleted) {
                            // 출석체크가 이미 완료된 경우
                            btn_attendcheck.setEnabled(false);    // 출석체크 버튼을 사라지게 설정
                            calendarView.setAlpha(0.3f);    // 캘린더뷰를 반투명하게 설정
                            attendanceCompletedTextView.setVisibility(View.VISIBLE);    // 출석체크 완료 텍스트뷰 보이게 설정
                        } else {
                            // 출석체크가 완료되지 않은 경우
                            btn_attendcheck.setVisibility(View.VISIBLE);    // 출석체크 버튼을 보이게 설정
                            calendarView.setAlpha(1.0f);    // 캘린더뷰를 다시 불투명하게 설정
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 데이터베이스 오류 처리
                    }
                });
            }
        });

        // 출석체크 버튼 클릭 시 호출되는 리스너 설정
        btn_attendcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 날짜의 밀리초값을 가져와서 Calendar 객체로 변환
                long selectedDateInMillis = calendarView.getDate();
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.setTimeInMillis(selectedDateInMillis);
                int year = selectedCalendar.get(Calendar.YEAR);
                int month = selectedCalendar.get(Calendar.MONTH);
                int dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                // 선택된 날짜를 문자열로 변환햐여 Firebase에 출석체크 완료로 데이터 기록
                String selectedDate = formatDate(year, month, dayOfMonth);

                userRef.child("attendance").child(selectedDate).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean attendanceCompleted = dataSnapshot.getValue(Boolean.class);
                        if (attendanceCompleted != null && attendanceCompleted) {
                            // 출석체크가 이미 완료된 경우
                            attendanceCompletedTextView.setVisibility(View.VISIBLE);    // 출석체크 완료 텍스트뷰 보이게 설정
                            btn_attendcheck.setEnabled(false);
                            // 캘린더뷰는 안 보이게, 출석체크 완료 텍스트뷰는 보이게 설정
                            calendarView.setAlpha(0.3f);    // 캘린더뷰를 반투명하게 설정
                        } else {
                            // 출석체크가 완료되지 않은 경우
                            // 출석체크 완료 처리하고 메시지 표시
                            markAttendanceCompletedForDate(selectedDate);
                            Toast.makeText(AttendanceActivity.this, "출석체크가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                            btn_attendcheck.setEnabled(false);
                            calendarView.setAlpha(0.3f);    // 캘린더뷰를 반투명하게 설정
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 데이터베이스 오류 처리
                    }
                });
            }
        });
    }

    // 날짜를 "yyyy-MM-dd" 형식의 문자열로 변환하는 메서드
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    // 선택된 날짜의 출석체크 완료를 Firebase에 저장하는 메서드
    private void markAttendanceCompletedForDate(String date) {
        userRef.child("attendance").child(date).setValue(true);
    }
}
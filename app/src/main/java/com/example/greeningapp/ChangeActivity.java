package com.example.greeningapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // 파이어베이스 인증 처리
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private EditText mEtName, mEtPostcode, mEtAddress, mEtEmail, mEtPhone;
    private Button mBtnSave;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        // 액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        backButton = findViewById(R.id.back_ic);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        //뒤로가기

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UserAccount").child("UserAccount");

        mEtName = findViewById(R.id.et_name);
        mEtPostcode = findViewById(R.id.et_postcode);
        mEtAddress = findViewById(R.id.et_address);
        mEtEmail = findViewById(R.id.et_email);
        mEtPhone = findViewById(R.id.et_phone);
//        mEtPwd = findViewById(R.id.et_pwd);
        mBtnSave = findViewById(R.id.save_btn);

        // 사용자 정보 가져오기
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = mDatabaseRef.child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("username").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        String email = dataSnapshot.child("emailId").getValue(String.class);
                        String postcode = dataSnapshot.child("postcode").getValue(String.class);
                        String address = dataSnapshot.child("address").getValue(String.class);

                        mEtName.setText(name);
                        mEtPhone.setText(phone);
                        mEtEmail.setText(email);
                        mEtPostcode.setText(postcode);
                        mEtAddress.setText(address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ChangeActivity.this, "회원정보를 불러오는데에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // 저장 버튼 클릭 이벤트 처리
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        // 변경된 정보 가져오기
        String name = mEtName.getText().toString().trim();
        String postcode = mEtPostcode.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
//        String password = mEtpwd.getText().toString().trim();

        // 변경된 정보 업데이트
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference userRef = mDatabaseRef.child(uid);
            userRef.child("username").setValue(name);
            userRef.child("postcode").setValue(postcode);
            userRef.child("address").setValue(address);
//            userRef.child("password").setValue(password);

            Toast.makeText(ChangeActivity.this, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
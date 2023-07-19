package com.example.greeningapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PwdChangeActivity extends AppCompatActivity {


//    private EditText mEtEmail;
//    private Button mBtnReset;
//    private FirebaseAuth mFirebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pwdchange);
//
//        // 액션바 숨기기
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
//
//        mEtEmail = findViewById(R.id.et_email);
//        mBtnReset = findViewById(R.id.btn_reset);
//        mFirebaseAuth = FirebaseAuth.getInstance();
//
//        mBtnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetPassword();
//            }
//        });
//    }
//
//    private void resetPassword() {
//        String email = mEtEmail.getText().toString().trim();
//
//        mFirebaseAuth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ResetPasswordActivity.this, "비밀번호 재설정 이메일이 전송되었습니다.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ResetPasswordActivity.this, "비밀번호 재설정 이메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
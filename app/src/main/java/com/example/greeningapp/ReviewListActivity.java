package com.example.greeningapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity {

    private RecyclerView reviewRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private FirebaseDatabase database;
    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

//        Toolbar toolbar = findViewById(R.id.toolbar);    // 툴바 아이디 연결
//        setSupportActionBar(toolbar);    // 액티비티의 앱바로 지정
//        ActionBar actionBar = getSupportActionBar();    // 앱바 제어를 위해 툴바 액세스
//        actionBar.setTitle("");    // 툴바 제목 설정
//        actionBar.setDisplayHomeAsUpEnabled(true);    // 앱바에 뒤로가기 버튼 만들기

        // 리사이클러뷰 초기화
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList, this);
        reviewRecyclerView.setAdapter(reviewAdapter);

        // 인텐트에서 전달된 상품 ID 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pid")) {
            pid = intent.getIntExtra("pid", 0);
        }

        // Firebase 데이터베이스 초기화 및 Review 데이터 조회
        database = FirebaseDatabase.getInstance();
        Query reviewQuery = database.getReference("Review").orderByChild("pid").equalTo(pid);
        reviewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // 데이터베이스에서 리뷰 객체 가져오기
                    Review review = snapshot.getValue(Review.class);
                    reviewList.add(review);
                }
                reviewAdapter.notifyDataSetChanged();    // 어댑터에 데이터 변경 알림
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 조회 중 에러 발생 시
                Log.e("ReviewListActivity", databaseError.getMessage());
            }
        });
    }
}
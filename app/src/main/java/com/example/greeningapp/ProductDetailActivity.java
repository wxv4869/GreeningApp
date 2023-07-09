package com.example.greeningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView pimg;
    private ImageView pdetailimg;
    private TextView pname;
    private TextView pprice;
    private TextView stock;

    private Button btnMinus;
    private Button btnPlus;
    private TextView quantityCount;

    private int count = 0;

    private int pid;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button moreReviewsButton;
    private ReviewAdapter adapter;
    private ArrayList<Review> arrayList;
    private FirebaseDatabase database;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);    // 툴바 아이디 연결
        setSupportActionBar(toolbar);    // 액티비티의 앱바로 지정
        ActionBar actionBar = getSupportActionBar();    // 앱바 제어를 위해 툴바 액세스
        actionBar.setTitle("");    // 툴바 제목 설정
        actionBar.setDisplayHomeAsUpEnabled(true);    // 앱바에 뒤로가기 버튼 만들기

        // 수량 조절 버튼 및 텍스트뷰 초기화
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        quantityCount = findViewById(R.id.quantity_count);

        // 수량 감소 버튼 클릭 시 이벤트 처리
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        // 수량 증가 버튼 클릭 시 이벤트 처리
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        // Intent에서 전달된 Product 객체와 데이터 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Product")) {
            Product product = intent.getParcelableExtra("Product");

            // 상품 상세 정보를 표시할 뷰 초기화
            pimg = findViewById(R.id.pimg);
            pname = findViewById(R.id.pname);
            pprice = findViewById(R.id.pprice);
            stock = findViewById(R.id.stock);
            pdetailimg = findViewById(R.id.pdetailimg);

            pid = intent.getIntExtra("pid", 0);

            // Product 객체의 데이터를 상세 정보 페이지에 표시
            Glide.with(this)
                    .load(product.getPimg())
                    .into(pimg);
            Glide.with(this)
                    .load(product.getPdetailimg())
                    .into(pdetailimg);
            pname.setText(product.getPname());
            pprice.setText("가격 | " + String.valueOf(product.getPprice()) + "원");
            stock.setText("재고수량 | " + String.valueOf(product.getStock()) + "개");

            pid = intent.getIntExtra("pid", 0);
        }

        // 더 많은 리뷰 보기 버튼 및 리사이클러뷰 초기화
        moreReviewsButton = findViewById(R.id.moreReviewsButton);
        recyclerView = findViewById(R.id.recyclerView);    // 아이디 연결
        recyclerView.setHasFixedSize(true);    // 리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        arrayList = new ArrayList<>();    // 리뷰 데이터 초기화

        // Firebase 데이터베이스 초기화 및 리뷰 데이터 조회
        database = FirebaseDatabase.getInstance();
        Query reviewQuery = database.getReference("Review").orderByChild("pid").equalTo(pid);
        reviewQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                int count = 0;    // 데이터 개수를 세는 변수
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (count >= 3)    // 3개의 데이터만 가져오기
                        break;

                    Review review = snapshot.getValue(Review.class);
                    arrayList.add(review);
                    count++;    // 데이터 개수 증가
                }
                adapter.notifyDataSetChanged();    // 어댑터에 데이터 변경 알림
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생 시
                Log.e("ProductDetailActivity", String.valueOf(databaseError.toException()));    // 에러문 출력
            }
        });

        adapter = new ReviewAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        // 더 많은 리뷰 보기 버튼 클릭 시
        moreReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리뷰 목록 액티비티로 전환
                Intent intent = new Intent(ProductDetailActivity.this, ReviewListActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:    //툴바 뒤로가기버튼 눌렸을 때 동작
                // ProductListActivity로 전환
                Intent intent = new Intent(ProductDetailActivity.this, ProductListActivity.class);
                startActivity(intent);
                finish ();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void increaseQuantity() {
        // btnPlus 클릭 시 quantityCount 증가
        count++;
        quantityCount.setText(String.valueOf(count));
    }

    private void decreaseQuantity() {
        // btnMinus 클릭 시 quantityCount 감소
        if (count > 0) {
            count--;
            quantityCount.setText(String.valueOf(count));
        }
    }
}
package com.example.greeningapp;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Product> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);    // 리사이클러뷰 아이디 연결
        recyclerView.setHasFixedSize(true);    // 리사이클러뷰 성능 개선
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();    // Product 객체를 담을 어레이리스트

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product");  // 파이어베이스 데이터베이스에서 "Product" 참조 가져오기
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();    // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {    // 반복문으로 데이터 리스트를 추출
                    Product product = snapshot.getValue(Product.class);    // 데이터를 Product 객체에 담는다.
                    arrayList.add(product);    // 담은 데이터들을 어레이리스트에 추가하여 리사이클러뷰로 전송할 준비
                }
                adapter.notifyDataSetChanged();    // 어댑터에 데이터 변경 알림을 보내어 업데이트
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // DB를 가져오던 중 에러 발생 시
                Log.e("FirebaseFail", String.valueOf(databaseError.toException()));    // 에러문 출력
            }
        });

        // 어댑터 객체 생성 및 초기화
        adapter = new ProductAdapter(arrayList, this, new ProductAdapter.OnItemClickListener() {
            public void onItemClick(Product product) {    // 아이템 클릭 이벤트 처리

            }
        });
        recyclerView.setAdapter(adapter);    // 리사이클러뷰에 어댑터 설정
    }
}
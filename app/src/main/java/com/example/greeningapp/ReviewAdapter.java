package com.example.greeningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>{
    private ArrayList<Review> dataList;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;


    public ReviewAdapter(ArrayList<Review> dataList , FirebaseAuth mFirebaseAuth, DatabaseReference mDatabaseRef ) {
        this.dataList = dataList;
        this.mFirebaseAuth = mFirebaseAuth;
        this.mDatabaseRef = mDatabaseRef;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if (dataList.get(position).getRimage() != null && !dataList.get(position).getRimage().isEmpty()) {
            // 이미지가 있는 경우 표시
            holder.inputimg.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView)
                    .load(dataList.get(position).getRimage())
                    .into(holder.inputimg);
        } else {
            // 이미지가 없는 경우 숨김
            holder.inputimg.setVisibility(View.GONE);
        }
        Glide.with(holder.itemView)
                .load(dataList.get(position).getRimage())
                .into(holder.inputimg);
        holder.reviewdes.setText(String.valueOf(dataList.get(position).getRcontent()));
        holder.userrating.setRating(dataList.get(position).getRscore());
        holder.reviewdate.setText(dataList.get(position).getRdatetime());
        holder.username.setText(dataList.get(position).getUsername());
        holder.reviewproductname.setText(dataList.get(position).getPname());

        // 사용자 이름 업데이트
//        FirebaseUser user = mFirebaseAuth.getCurrentUser();
//        if (user != null) {
//            String uid = dataList.get(position).getIdToken();
//            DatabaseReference userRef = mDatabaseRef.child(uid);
//            userRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        String name = dataSnapshot.child("username").getValue(String.class) ;
//                        holder.username.setText(name);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // 에러 처리 코드 추가
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        //삼합연산자
        return (dataList !=null ? dataList.size() :0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView inputimg;
        RatingBar userrating;
        TextView username;
        TextView reviewdes;
        TextView reviewdate, reviewproductname;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.inputimg = itemView.findViewById(R.id.inputimg);
            this.username = itemView.findViewById(R.id.username);
            this.reviewdes = itemView.findViewById(R.id.reviewdes);
            this.userrating = itemView.findViewById(R.id.userrating);
            this.reviewdate = itemView.findViewById(R.id.reviewdate);
            this.reviewproductname = itemView.findViewById(R.id.reviewproductname);

        }
    }


}
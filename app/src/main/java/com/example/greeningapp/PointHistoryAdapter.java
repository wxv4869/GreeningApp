package com.example.greeningapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;

import java.util.List;

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.PointHistoryViewHolder> {

    Context context;
    List<MyPoint> pointHistoryList;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    public PointHistoryAdapter(Context context, List<MyPoint> pointHistoryList) {
        this.context = context;
        this.pointHistoryList = pointHistoryList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public PointHistoryAdapter.PointHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_point_list, parent, false);
        PointHistoryViewHolder holder = new PointHistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointHistoryAdapter.PointHistoryViewHolder holder, @SuppressLint("recyclerview_pointHistory") int position) {
        MyPoint myPoint = pointHistoryList.get(position);

        holder.donationNameTextView.setText("기부 - " + myPoint.getDonationName());
        holder.donationDateTextView.setText(myPoint.getDonationDate());
        holder.donationPointTextView.setText(myPoint.getDonationPoint() + "씨드");
    }

    @Override
    public int getItemCount() {
        if (pointHistoryList != null) {
            return pointHistoryList.size();
        }
        return 0;
    }

    public class PointHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView donationNameTextView;
        private TextView donationDateTextView;
        private TextView donationPointTextView;

        public PointHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            donationNameTextView = itemView.findViewById(R.id.donationNameTextView);
            donationDateTextView = itemView.findViewById(R.id.donationDateTextView);
            donationPointTextView = itemView.findViewById(R.id.donationPointTextView);
        }

        public void bind(MyPoint myPoint) {
            donationNameTextView.setText(myPoint.getDonationName());
            donationDateTextView.setText(myPoint.getDonationDate());
            donationPointTextView.setText(String.valueOf(myPoint.getDonationPoint()));
        }
    }
}

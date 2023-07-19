package com.example.greeningapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CustomAdapter extends FirebaseRecyclerAdapter<item_list,CustomAdapter.myviewholder> {

    public CustomAdapter(@NonNull FirebaseRecyclerOptions<item_list> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull item_list item_list) {
        holder.p_name.setText(item_list.getP_name());
        holder.p_say.setText(item_list.getP_say());
        holder.p_price.setText(item_list.getP_price());
        Glide.with(holder.p_pic.getContext()).load(item_list.getP_pic()).into(holder.p_pic);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {
        ImageView p_pic;
        TextView p_name, p_say, p_price;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            p_pic = (ImageView) itemView.findViewById(R.id.p_pic);
            p_name = (TextView) itemView.findViewById(R.id.p_name);
            p_say = (TextView) itemView.findViewById(R.id.p_say);
            p_price = (TextView) itemView.findViewById(R.id.p_price);

        }
    }
}
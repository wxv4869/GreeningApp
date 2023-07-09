package com.example.greeningapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private ArrayList<Product> arrayList;
    private Context context;
    private OnItemClickListener listener;

    // 생성자
    public ProductAdapter(ArrayList<Product> arrayList, Context context, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        // itemView를 위한 새로운 뷰 홀더 객체 생성 및 반환
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_product, parent, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 뷰 홀더에 데이터 바인딩
        // 해당 포지션의 아이템 데이터를 가져와서 뷰에 표시함
        Product product = arrayList.get(position);

        // Glide로 이미지 로드 및 표시
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPimg())
                .into(holder.pimg);
        holder.pname.setText("상품명 : " + arrayList.get(position).getPname());
        holder.pprice.setText("가격 : " + String.valueOf(arrayList.get(position).getPprice()) + "원");
        holder.stock.setText("재고수량 : " + String.valueOf(arrayList.get(position).getStock()) + "개");

        // itemView 클릭 이벤트 처리
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상세 화면으로 이동하는 인텐트 생성 및 전달할 데이터 설정
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("Product", product);
                intent.putExtra("pid", product.getPid());
                context.startActivity(intent);

                // 클릭 이벤트 리스너 호출
                if (listener != null) {
                    listener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // 데이터 개수 반환
        return arrayList.size();
    }

    // itemView를 저장하는 뷰 홀더 클래스
    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView pimg;
        TextView pname;
        TextView pprice;
        TextView stock;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // itemView의 각 요소를 뷰 홀더에 연결
            this.pimg = itemView.findViewById(R.id.pimg);
            this.pname = itemView.findViewById(R.id.pname);
            this.pprice = itemView.findViewById(R.id.pprice);
            this.stock = itemView.findViewById(R.id.stock);
        }
    }

    // 아이템 클릭 이벤트를 처리하기 위한 인터페이스
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
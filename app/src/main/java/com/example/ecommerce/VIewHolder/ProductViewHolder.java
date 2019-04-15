package com.example.ecommerce.VIewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

//class ini menyimpan referensi dari view-view yang digunakan pada sebuah item direcycle view.
//viewholder = menyimpan view2 yang nantinya akan digunakan untuk menampikan data
//tujuannyamenghemat waktu dibandingkan dengan memakai findbyid() saat update list dengan data yang baru
public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtProductName, txtProductDescription, txtproductPrice;
    public ImageView imageView;
    public ItemClickListener listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_desc);
        txtproductPrice = (TextView)itemView.findViewById(R.id.product_price);
    }
    //ItemClickListener import interface import com.example.ecommerce.Interface.ItemClickListener;
    public void setItemClickListenter(ItemClickListener listner){
        this.listner =listner;
    }
    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(),false);
    }
}

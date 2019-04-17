package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private Button addToCartBtn;
    private ImageView product_Image;
    private ElegantNumberButton numberBtn;
    private TextView productPriceDetail, prouctDescriptionDetail, productNameDetail;
    private String productID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productID =getIntent().getStringExtra("pid");
        numberBtn = (ElegantNumberButton)findViewById(R.id.number_btn);
        product_Image = (ImageView) findViewById(R.id.product_image_detail);
        productPriceDetail= (TextView) findViewById(R.id.product_price_detail);
        productNameDetail = (TextView) findViewById(R.id.product_name_detail);
        prouctDescriptionDetail = (TextView) findViewById(R.id.product_description_detail);
        addToCartBtn = (Button)findViewById(R.id.pd_add_to_cart_btn);

        getProductDetail(productID);

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("cart list");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("productName", productNameDetail.getText().toString());
        cartMap.put("price", productPriceDetail.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberBtn.getNumber());
        cartMap.put("discount", "");
        cartListRef.child("User View").child(Prevalent.CurrentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetail(String productID) {
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Products");
        productsref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Product product = dataSnapshot.getValue(Product.class);
                    productNameDetail.setText(product.getProductname());
                    productPriceDetail.setText(product.getPrice());
                    prouctDescriptionDetail.setText(product.getDescription());
                    Picasso.get().load(product.getImage()).into(product_Image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

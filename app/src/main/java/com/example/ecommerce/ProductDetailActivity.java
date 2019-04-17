package com.example.ecommerce;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class ProductDetailActivity extends AppCompatActivity {
    private FloatingActionButton addtoCart;
    private ImageView product_Image;
    private ElegantNumberButton numberBtn;
    private TextView productPrice, prouctDescription, productName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        addtoCart = (FloatingActionButton)findViewById(R.id.add_product_to_cart_btn);
        numberBtn = (ElegantNumberButton)findViewById(R.id.number_btn);
        product_Image = (ImageView) findViewById(R.id.product_image_detail);
        productPrice= (TextView) findViewById(R.id.product_price_detail);
        productName = (TextView) findViewById(R.id.product_name_detail);
    }
}

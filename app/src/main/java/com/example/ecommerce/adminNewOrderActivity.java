package com.example.ecommerce;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewholder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewholder holder, int position, @NonNull AdminOrders model) {
                        holder.userName.setText("Name = " + model.getName());
                        holder.userPhoneNumber.setText("Phone = " + model.getPhone());
                        holder.userTotalAmount.setText("Total Amount= " + model.getTotalAmount());
                        holder.userDateTime.setText("Order at = " + model.getDate() + " " + model.getTime());
                        holder.userShippingAddress.setText("Shipping address = " + model.getAddress() + " " + model.getCity());
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewholder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewholder extends RecyclerView.ViewHolder{
        public TextView userName, userPhoneNumber,userTotalAmount, userDateTime, userShippingAddress;
        public Button showOrdersBtn;
        public AdminOrdersViewholder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber= itemView.findViewById(R.id.order_phone_number);
            userTotalAmount = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            showOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);

        }
    }
}

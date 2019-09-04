package com.example.asimkhan.eating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asimkhan.eating.Interface.ItemClickListener;
import com.example.asimkhan.eating.Model.Requests;
import com.example.asimkhan.eating.Viewholder.OrderViewHolder;
import com.example.asimkhan.eating.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersStatus extends AppCompatActivity {
    private RecyclerView orderslist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference orderstatus;
    FirebaseRecyclerOptions<Requests> options;
    FirebaseRecyclerAdapter<Requests, OrderViewHolder> adapter;
    Requests requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_status);
        //firebase setup...
        requests = new Requests();
        firebaseDatabase = FirebaseDatabase.getInstance();
        orderstatus = firebaseDatabase.getReference("Requests");
        //Recycler set up...
        orderslist = (RecyclerView) findViewById(R.id.orderslist);
        orderslist.setHasFixedSize(true);
        if (getIntent() != null)
            loadOrders(Common.currentuser.getPhone());
        else
            loadOrders(getIntent().getStringExtra("userphone"));

    }

    private void loadOrders(String phone) {
        options = new FirebaseRecyclerOptions.Builder<Requests>().setQuery(orderstatus.
                orderByChild("phone").equalTo(phone), Requests.class).build();
        adapter = new FirebaseRecyclerAdapter<Requests, OrderViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Requests model) {
                holder.order_id.setText(adapter.getRef(position).getKey());
                holder.order_status.setText(Common.convertcodetostatus(model.getStatus()));
                holder.order_phone.setText(model.getPhone());
                holder.order_address.setText(model.getAddress());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(OrdersStatus.this,"Click not allowed",Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.orderlist_item, parent, false);

                return new OrderViewHolder(view);
            }

        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        orderslist.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        orderslist.setAdapter(adapter);
    }


}

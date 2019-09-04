package com.example.asimkhan.eating;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asimkhan.eating.Database.Database;
import com.example.asimkhan.eating.Model.Order;
import com.example.asimkhan.eating.Model.Requests;
import com.example.asimkhan.eating.Viewholder.CartAdapter;
import com.example.asimkhan.eating.common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    private RecyclerView cartlist;
    RecyclerView.LayoutManager layoutManager;
    TextView totaltxt;
    Button placeorderbtn;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference Request;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //firebase...
        firebaseDatabase = FirebaseDatabase.getInstance();
        Request = firebaseDatabase.getReference("Requests");
        database = new Database(this);
        //init...
        cartlist = (RecyclerView) findViewById(R.id.cart_recyler);
        cartlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartlist.setLayoutManager(layoutManager);
        totaltxt = (TextView) findViewById(R.id.total);
        placeorderbtn = (Button) findViewById(R.id.orderplace);
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialogue();
            }
        });
        Loadfoodlist();

    }

    private void alertdialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setTitle("one more step!");
        builder.setMessage("Enter your Address");
        //Adding edit text to alert dialogue...
        final EditText editadress = new EditText(Cart.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editadress.setLayoutParams(params);
        builder.setView(editadress);//add edit text to alert dialogue...
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Requests requests = new Requests(
                        Common.currentuser.getPhone(),
                        Common.currentuser.getName(),
                        editadress.getText().toString(),
                        totaltxt.getText().toString(),
                        cart
                );
                Request.child(String.valueOf(System.currentTimeMillis())).
                        setValue(requests);

                //delete cart
                database.cleancart();
                Toast.makeText(Cart.this, "Thank you order place",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void Loadfoodlist() {
        cart = database.getcarts();
        adapter = new CartAdapter(cart, this);
        cartlist.setAdapter(adapter);
        //calculate total price...
        int total = 0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "us");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            totaltxt.setText(numberFormat.format(total));
        }
    }
}

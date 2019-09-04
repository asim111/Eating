package com.example.asimkhan.eating;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.asimkhan.eating.Database.Database;
import com.example.asimkhan.eating.Model.Food;
import com.example.asimkhan.eating.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    private android.widget.ImageView foodimg;
    private androidx.appcompat.widget.Toolbar toolbar;
    private CollapsingToolbarLayout collapsingtoolbar;
    private AppBarLayout appbarlayout;
    private FloatingActionButton btncart;
    private android.widget.TextView foodnamesecond;
    private android.widget.TextView foodprice;
    private android.widget.LinearLayout layoutprice;
    private com.cepheuen.elegantnumberbutton.view.ElegantNumberButton numberbtn;
    private android.widget.TextView fooddescription;
    private androidx.core.widget.NestedScrollView nestedscrollview;
    private String foodid = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference food;
    private Food currentfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        init();
        if (getIntent() != null) {
            foodid = getIntent().getStringExtra("FoodId");
            if (!foodid.isEmpty()) {
                getfooddetail(foodid);
            }
        }
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addtocart(new Order(
                        foodid,
                        currentfood.getName(),
                        numberbtn.getNumber(),
                        currentfood.getPrice(),
                        currentfood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this, "Add to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getfooddetail(String foodid) {
        food.child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentfood = dataSnapshot.getValue(Food.class);
                Picasso.get().load(currentfood.getImage()).into(foodimg);
                collapsingtoolbar.setTitle(currentfood.getName());
                foodprice.setText(currentfood.getPrice());
                foodnamesecond.setText(currentfood.getName());
                fooddescription.setText(currentfood.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        food = firebaseDatabase.getReference("Foods");
        // init view...
        this.btncart = (FloatingActionButton) findViewById(R.id.btn_cart);
        this.nestedscrollview = (NestedScrollView) findViewById(R.id.nestedscrollview);
        this.fooddescription = (TextView) findViewById(R.id.food_description);
        this.numberbtn = (ElegantNumberButton) findViewById(R.id.number_btn);
        this.layoutprice = (LinearLayout) findViewById(R.id.layout_price);
        this.foodprice = (TextView) findViewById(R.id.food_price);
        this.foodnamesecond = (TextView) findViewById(R.id.food_name_second);
        this.appbarlayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        this.collapsingtoolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.foodimg = (ImageView) findViewById(R.id.food_img);
        collapsingtoolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingtoolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
    }
}

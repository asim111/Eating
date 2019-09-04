package com.example.asimkhan.eating;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asimkhan.eating.Interface.ItemClickListener;
import com.example.asimkhan.eating.Model.Food;
import com.example.asimkhan.eating.Viewholder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Foodlist extends AppCompatActivity {
    private RecyclerView foodrecycler;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference foodlist;
    private String catogeryid;

    FirebaseRecyclerOptions<Food> options;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    //adapter for searching purpose...
    FirebaseRecyclerOptions<Food> searchoptions;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchadapter;
    private List<String> suggestlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodlist);

        //firebase setup...
        firebaseDatabase = FirebaseDatabase.getInstance();
        foodlist = firebaseDatabase.getReference("Foods");

        //Recycler set up...
        foodrecycler = (RecyclerView) findViewById(R.id.food_recycler);
        foodrecycler.setHasFixedSize(true);

        //search functionality...

        suggestlist = new ArrayList<>();
        final MaterialSearchBar materialSearchBar;

        //get the catogery id here...
        if (getIntent() != null) {
            catogeryid = getIntent().getStringExtra("Catogeryid");
        }
        if (!catogeryid.isEmpty() && catogeryid != null) {
            loadFoodList(catogeryid);
        }

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.newsearchbar);
        loadsuggest();
        materialSearchBar.setLastSuggestions(suggestlist);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type text we will change the suggestion in the suggest list...
                List<String> suggest = new ArrayList<>();
                for (String search : suggestlist)//loop in suggestlist....
                {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when user close the search bar
                //then restore the origional adapter...
                if (!enabled) {
                    foodrecycler.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish show the result of search on adapter...
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchoptions = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlist.orderByChild("name")
                .equalTo(text.toString()), Food.class).build();
        searchadapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(searchoptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                final Food local = model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fooddetail = new Intent(Foodlist.this, FoodDetail.class);
                        fooddetail.putExtra("FoodId", searchadapter.getRef(position).getKey());
                        startActivity(fooddetail);

                    }
                });
                holder.foodname.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.foodimage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(Foodlist.this, "error while showing the image ",
                                Toast.LENGTH_LONG).show();
                    }


                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        foodrecycler.setLayoutManager(gridLayoutManager);
        searchadapter.startListening();
        foodrecycler.setAdapter(searchadapter);
    }

    //  load suggestions from firebase...
    private void loadsuggest() {
        foodlist.orderByChild("menuId").equalTo(catogeryid).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestlist.add(item.getName());//getting the name of food to suggest list...
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadFoodList(String catogeryid) {
        options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodlist.orderByChild("menuId")
                .equalTo(catogeryid), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                final Food local = model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fooddetail = new Intent(Foodlist.this, FoodDetail.class);
                        fooddetail.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(fooddetail);

                    }
                });
                holder.foodname.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.foodimage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(Foodlist.this, "error while showing the image ",
                                Toast.LENGTH_LONG).show();
                    }


                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        foodrecycler.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        foodrecycler.setAdapter(adapter);

    }
}

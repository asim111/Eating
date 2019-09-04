package com.example.asimkhan.eating;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asimkhan.eating.Interface.ItemClickListener;
import com.example.asimkhan.eating.Model.Category;
import com.example.asimkhan.eating.Model.Requests;
import com.example.asimkhan.eating.Service.ListenOrder;
import com.example.asimkhan.eating.Viewholder.MenuViewHolder;
import com.example.asimkhan.eating.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
private FirebaseDatabase database;
private DatabaseReference catogery;
TextView userfullname;
RecyclerView recycler_menu;
RecyclerView.LayoutManager layoutManager;
FirebaseRecyclerOptions<Category> options;
FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    Requests requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //init firebase...

        database = FirebaseDatabase.getInstance();
        catogery = database.getReference("Category");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent = new Intent(Home.this,Cart.class);
                startActivity(cartintent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //set Name for user...
        View headerView = navigationView.getHeaderView(0);

        //set user name on the navigation header...
        userfullname = (TextView)headerView.findViewById(R.id.user_full_name_on_nav_header);
        userfullname.setText(Common.currentuser.getName());

        //Load menu...
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
      //layoutManager = new LinearLayoutManager(this);
       //recycler_menu.setLayoutManager(layoutManager);
       loadMenu();

       //Register service
        Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);
    }

    private void loadMenu() {
        options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(catogery,Category.class).build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, final int position, @NonNull Category model) {
                Picasso.get().load(model.getImage()).into(holder.menuimage);
                holder.txtmenuname.setText(model.getName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodlist = new Intent(Home.this,Foodlist.class);
                        foodlist.putExtra("Catogeryid",adapter.getRef(position).getKey());
                        startActivity(foodlist);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);

                return new MenuViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recycler_menu.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//         Handle action bar item clicks here. The action bar will
//        automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            Intent cartintent = new Intent(Home.this,Cart.class);
            startActivity(cartintent);

        } else if (id == R.id.nav_order) {
           Intent orderstatusintent = new Intent(Home.this,OrdersStatus.class);
        startActivity(orderstatusintent);

        } else if (id == R.id.nav_logout) {
            Intent logoutintent = new Intent(Home.this,SignIn.class);
            logoutintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutintent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

package com.example.asimkhan.eating.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.asimkhan.eating.Model.Order;
import com.example.asimkhan.eating.R;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String db_name = "eatingdb.db";
    private static final int db_var = 1;

    public Database(Context context) {
        super(context, db_name, null, db_var);
    }

    public List<Order> getcarts() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlselect = {"Productid", "Productname", "Quantity", "Price"
                , "Discount"};
        String sqltable = "Foodorderdetail";
        qb.setTables(sqltable);
        Cursor c = qb.query(db, sqlselect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("Productid")),
                        c.getString(c.getColumnIndex("Productname")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))));
            } while (c.moveToNext());
        }
        return result;
    }

    //inserting the values in to values...
    public void addtocart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Foodorderdetail(Productid," +
                        "Productname,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductid(),
                order.getProductname(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void cleancart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Foodorderdetail");
        db.execSQL(query);
    }
}

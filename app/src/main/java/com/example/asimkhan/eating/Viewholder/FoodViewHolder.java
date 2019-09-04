package com.example.asimkhan.eating.Viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asimkhan.eating.Interface.ItemClickListener;
import com.example.asimkhan.eating.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView foodname;
    public ImageView foodimage;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        foodname = (TextView)itemView.findViewById(R.id.food_name);
        foodimage = (ImageView)itemView.findViewById(R.id.food_iamge);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

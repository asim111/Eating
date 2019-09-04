package com.example.asimkhan.eating.Viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asimkhan.eating.Interface.ItemClickListener;
import com.example.asimkhan.eating.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtmenuname;
    public ImageView menuimage;
    private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {

        super(itemView);
        txtmenuname = (TextView)itemView.findViewById(R.id.menu_name);
        menuimage = (ImageView)itemView.findViewById(R.id.menu_iamge);
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

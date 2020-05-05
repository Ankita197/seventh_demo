package com.ankita.seventask.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.seventask.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemCamaraImageAdapter extends RecyclerView.Adapter<ItemCamaraImageAdapter.ViewHolder> {

    Context context;
    ArrayList<Photo> arrayList;

    public ItemCamaraImageAdapter(Context context, ArrayList<Photo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Glide.with(context).load(arrayList.get(position).getUri()).into(holder.ivImage);
       // Bitmap myBitmap = BitmapFactory.decodeFile(arrayList.get(position).getUri());
        holder.ivImage.setImageBitmap(arrayList.get(position).getUri());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage=itemView.findViewById(R.id.ivImage);
        }
    }
}

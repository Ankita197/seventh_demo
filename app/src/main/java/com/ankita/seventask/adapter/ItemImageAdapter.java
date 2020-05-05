package com.ankita.seventask.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ankita.seventask.R;
import com.ankita.seventask.ZoomActivity;
import com.ankita.seventask.databinding.ItemImageBinding;
import com.ankita.seventask.repository.CreateResponse;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemImageAdapter extends RecyclerView.Adapter<ItemImageAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<CreateResponse.User> userlist;

    public ItemImageAdapter(Context context, ArrayList<CreateResponse.User> userlist){
        this.context=context;
        this.userlist=userlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                LayoutInflater inflater=LayoutInflater.from(parent.getContext());
               ItemImageBinding binding= ItemImageBinding.inflate(inflater,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
//        holder.ivImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(context, ZoomActivity.class);
//                intent.putExtra("image_url",userlist.get(position).getImage());
//                context.startActivity(intent);
//            }
//        });
        CreateResponse.User data=userlist.get(position);
        holder.binding(data);

    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;
        ImageView ivImage;
        public MyViewHolder(@NonNull ItemImageBinding binding) {
            super(binding.getRoot());
            //ivImage=itemView.findViewById(R.id.ivImage);
            this.binding=binding;
        }

        public void binding(CreateResponse.User data) {
            binding.setModel(data);
            binding.executePendingBindings();
        }
    }
}

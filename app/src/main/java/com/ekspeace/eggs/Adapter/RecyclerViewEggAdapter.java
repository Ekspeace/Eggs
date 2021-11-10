package com.ekspeace.eggs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ekspeace.eggs.Model.Egg;
import com.ekspeace.eggs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewEggAdapter extends RecyclerView.Adapter<RecyclerViewEggAdapter.MyViewHolder> {
    private Context context;
    private List<Egg> eggs;
    private OnItemClickListener onItemClickListener;
    private int Position;

    public RecyclerViewEggAdapter(Context context, List<Egg> eggs) {
        this.context = context;
        this.eggs = eggs;
    }

 @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recycler_eggs, parent, false);
        return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(eggs.get(position).getName());
        String thumb = eggs.get(position).getImage();
        Picasso.get().load(thumb).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return eggs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.egg_name);
            image = itemView.findViewById(R.id.egg_image);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = MyViewHolder.this.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

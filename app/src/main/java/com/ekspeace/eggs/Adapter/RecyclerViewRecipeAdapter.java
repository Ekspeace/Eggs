package com.ekspeace.eggs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.eggs.Model.Recipe;
import com.ekspeace.eggs.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewRecipeAdapter extends RecyclerView.Adapter<RecyclerViewRecipeAdapter.MyViewHolder> {
private Context context;
private List<Recipe> recipeList;
private OnItemClickListener onItemClickListener;
private int Position;

public RecyclerViewRecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
        .inflate(R.layout.layout_recipe_item, parent, false);
        return new MyViewHolder(itemView);
}
@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(recipeList.get(position).getName());
        String thumb = recipeList.get(position).getImage();
        Picasso.get().load(thumb).into(holder.image);
}


@Override
public int getItemCount() {
        return recipeList.size();
}

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView name;
    private ImageView image;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.recipe_name);
        image = itemView.findViewById(R.id.recipe_image);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            int position = RecyclerViewRecipeAdapter.MyViewHolder.this.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(itemView, position);
            }
        }
    }
}
public interface OnItemClickListener {
    void onItemClick(View itemView, int position);
}
    public void setOnItemClickListener(RecyclerViewRecipeAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
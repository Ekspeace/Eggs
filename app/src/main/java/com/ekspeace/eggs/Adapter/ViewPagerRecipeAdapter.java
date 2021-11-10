package com.ekspeace.eggs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.ekspeace.eggs.Model.Recipe;
import com.ekspeace.eggs.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class ViewPagerRecipeAdapter extends PagerAdapter {
    private List<Recipe> recipes;
    private Context context;

    public ViewPagerRecipeAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_view_recipe,
                container,
                false
        );
        ImageView recipeThumb = view.findViewById(R.id.recipe_image);
        String strRecipeThumb = recipes.get(position).getImage();
        Picasso.get().load(strRecipeThumb).into(recipeThumb);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

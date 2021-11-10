package com.ekspeace.eggs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekspeace.eggs.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

public class RecipeDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        InitView();
        SetupActionBar();
    }
    private void InitView(){
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        ImageView imageView = findViewById(R.id.recipe_detail_image);
        TextView tvCook = findViewById(R.id.recipe_detail_cook_time);
        TextView tvPrep = findViewById(R.id.recipe_detail_prep_time);
        TextView tvServes = findViewById(R.id.recipe_detail_Serves);
        TextView tvIngredients = findViewById(R.id.recipe_detail_ingredients);
        TextView tvInstructions = findViewById(R.id.recipe_detail_instructions);

        Intent intent = getIntent();
        String name = intent.getStringExtra("NAME");
        String image = intent.getStringExtra("IMAGE");
        String cook = intent.getStringExtra("COOK");
        String prep = intent.getStringExtra("PREP");
        String serves = intent.getStringExtra("SERVES");
        String instructions = intent.getStringExtra("INSTRUCTIONS");
        String ingredients = intent.getStringExtra("INGREDIENTS");

        collapsingToolbarLayout.setTitle(name);
        Picasso.get().load(image).into(imageView);
        tvCook.setText(cook);
        tvPrep.setText(prep);
        tvServes.setText(serves);
        tvIngredients.setText(Html.fromHtml(ingredients));
        tvInstructions.setText(Html.fromHtml(instructions));
    }

    private void SetupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.brown_700));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
    }
}
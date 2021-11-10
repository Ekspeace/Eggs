package com.ekspeace.eggs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.R;
import com.ekspeace.eggs.ViewModel.BasketViewModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.ekspeace.eggs.Constants.Common.PRICE;

public class AddToBasket extends AppCompatActivity {
    private TextView tvHeading, tvSubHeading, tvProductPrice, tvProductQuantity;
    private ImageView ivButtonBack, ivProductImage;
    private ImageButton ibButtonAddQuantity, ibButtonSubtractQuantity;
    private RadioGroup rgRadioGroup;
    private RadioButton rbProductSize;
    private Button btnAddToBasket;
    private int count = 1;
    private double PriceHolder;
    private View layout;
    private BasketViewModel basketViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_basket);
        Intent intent = getIntent();
        String ProductName = intent.getStringExtra("NAME");
        String ProductImage = intent.getStringExtra("IMAGE");
        String ProductPrice = intent.getStringExtra("PRICE");

        InitView();
        SetHandlers(ProductImage, ProductName, ProductPrice);

    }
    private void InitView(){
        tvHeading = findViewById(R.id.add_title);
        tvSubHeading = findViewById(R.id.add_product_name);
        tvProductPrice = findViewById(R.id.add_product_price);
        tvProductQuantity = findViewById(R.id.add_number_quantity);

        ivButtonBack = findViewById(R.id.add_back);
        ivProductImage = findViewById(R.id.add_image);
        ibButtonAddQuantity = findViewById(R.id.add_positive_button);
        ibButtonSubtractQuantity = findViewById(R.id.add_negative_button);

        rgRadioGroup = findViewById(R.id.add_radio_group);
        btnAddToBasket = findViewById(R.id.add_to_basket);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
        basketViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BasketViewModel.class);
    }

    private void SetHandlers(String ProductImage, String ProductName, String ProductPrice){
        ivButtonBack.setOnClickListener(view -> onBackPressed());
        Picasso.get().load(ProductImage).into(ivProductImage);
        tvHeading.setText(ProductName.toUpperCase());
        tvSubHeading.setText(ProductName);
        tvProductPrice.setText(ProductPrice);

        DecimalFormat df = new DecimalFormat("####0.00");
        PriceHolder = Double.parseDouble(ProductPrice);
        tvProductQuantity.setText(Integer.toString(count));
        ibButtonAddQuantity.setOnClickListener(view -> {
            tvProductQuantity.setText(Integer.toString(++count));
            tvProductPrice.setText("R".concat(String.format("%.2f", PriceHolder += PRICE)));
        });
        ibButtonSubtractQuantity.setOnClickListener(view -> {
           if(count != 1 ) {
               tvProductQuantity.setText(Integer.toString(--count));
               tvProductPrice.setText("R".concat(String.format("%.2f", PriceHolder -= PRICE)));
           }
       });

        rgRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            rbProductSize = radioGroup.findViewById(i);
            rbProductSize = rbProductSize.findViewById(rgRadioGroup.getCheckedRadioButtonId());
        });

        btnAddToBasket.setOnClickListener(view -> {
            if(rbProductSize != null){
                String ProductSize = rbProductSize.getText().toString();
                Basket basket = new Basket(ProductName, count, ProductSize, PriceHolder, ProductImage);
                basketViewModel.insert(basket);
                PopUp.Toast(this, layout, "Successfully added product to basket", Toast.LENGTH_SHORT);
            }else {
                PopUp.Toast(this, layout, "Please specify the size of the product ", Toast.LENGTH_SHORT);
            }
        });
    }
}
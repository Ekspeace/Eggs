package com.ekspeace.eggs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.Model.Egg;
import com.ekspeace.eggs.Model.EventBus.AddQuantity;
import com.ekspeace.eggs.Model.EventBus.DeleteEvent;
import com.ekspeace.eggs.R;
import com.ekspeace.eggs.ViewModel.BasketViewModel;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ekspeace.eggs.Constants.Common.PRICE;

public class RecyclerViewItemsBasketAdapter extends RecyclerView.Adapter<RecyclerViewItemsBasketAdapter.MyViewHolder> {
    private Context context;
    private List<Basket> basketList;
    private BasketViewModel basketViewModel;
    private View layout;

    public RecyclerViewItemsBasketAdapter(Context context, List<Basket> basketList, BasketViewModel basketViewModel, View layout) {
        this.context = context;
        this.basketList = basketList;
        this.basketViewModel = basketViewModel;
        this.layout = layout;
    }

    @NonNull
    @Override
    public RecyclerViewItemsBasketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_basket_items, parent, false);
        return new RecyclerViewItemsBasketAdapter.MyViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemsBasketAdapter.MyViewHolder holder, int position) {
        Basket basket = basketList.get(position);
        holder.tvProductName.setText(basket.getEggName());
        holder.tvProductSize.setText(basket.getEggSize());
        holder.tvProductQuantity.setText(Integer.toString(basket.getEggQuantity()));
        holder.tvProductPrice.setText("R".concat(String.format("%.2f",basket.getEggPrice())));
        Picasso.get().load(basket.getEggImage()).into(holder.ivProductImage);

        holder.ivRemoveProduct.setOnClickListener(view ->
                PopUp.DeleteDialog(context,"Delete Item", "Are you sure, you want to delete this item ?", basketViewModel, basketList, position, layout));
    }
    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProductName, tvProductSize, tvProductQuantity, tvProductPrice;
        private ImageView ivProductImage, ivRemoveProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.basket_product_name);
            tvProductSize = itemView.findViewById(R.id.basket_product_size);
            tvProductQuantity = itemView.findViewById(R.id.basket_product_quantity);
            tvProductPrice = itemView.findViewById(R.id.basket_product_price);

            ivProductImage = itemView.findViewById(R.id.basket_product_image);
            ivRemoveProduct = itemView.findViewById(R.id.basket_delete_item);

        }
    }
}

package com.ekspeace.eggs.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.Model.HistoryOrder;
import com.ekspeace.eggs.R;
import com.ekspeace.eggs.ViewModel.BasketViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewHistoryOrderAdapter extends RecyclerView.Adapter<RecyclerViewHistoryOrderAdapter.MyViewHolder> {
    private Context context;
    private List<HistoryOrder> historyOrders;

    public RecyclerViewHistoryOrderAdapter(Context context,  List<HistoryOrder> historyOrders) {
        this.context = context;
        this.historyOrders = historyOrders;
    }

    @NonNull
    @Override
    public RecyclerViewHistoryOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_history_item, parent, false);
        return new RecyclerViewHistoryOrderAdapter.MyViewHolder(itemView);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHistoryOrderAdapter.MyViewHolder holder, int position) {
        HistoryOrder historyOrder = historyOrders.get(position);
        String items = "", quantities = "", costs = "";
        for(String item : historyOrder.getItems()){
            items += item.concat(", ");
        }
        holder.tvHistoryItem.setText(items);
        for(String quantity : historyOrder.getItemQuantity()){
            quantities += quantity.concat(", ");
        }
        holder.tvHistoryItemQuantity.setText(quantities);
        holder.tvHistoryDate.setText(historyOrder.getOrderDate());
        for(String cost : historyOrder.getCost()){
            costs += "R"+cost.concat("0, ");
        }
        holder.tvHistoryCost.setText(costs);
        holder.tvHistoryArrived.setText(historyOrder.isArrived());

        holder.ivHistoryDelete.setOnClickListener(view -> {

        });

    }
    @Override
    public int getItemCount() {
        return historyOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvHistoryItem, tvHistoryItemQuantity, tvHistoryDate, tvHistoryCost, tvHistoryArrived;
        private ImageView ivHistoryDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHistoryItem = itemView.findViewById(R.id.history_items);
            tvHistoryItemQuantity = itemView.findViewById(R.id.history_item_quantity);
            tvHistoryDate = itemView.findViewById(R.id.history_date);
            tvHistoryCost = itemView.findViewById(R.id.history_cost);
            tvHistoryArrived = itemView.findViewById(R.id.history_arrived);
            ivHistoryDelete = itemView.findViewById(R.id.history_delete);
        }
    }
}

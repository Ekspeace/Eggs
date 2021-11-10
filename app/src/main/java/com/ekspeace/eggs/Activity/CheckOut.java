package com.ekspeace.eggs.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Adapter.RecyclerViewItemsBasketAdapter;
import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.Model.EventBus.DeleteEvent;
import com.ekspeace.eggs.Model.HistoryOrder;
import com.ekspeace.eggs.Model.User;
import com.ekspeace.eggs.R;
import com.ekspeace.eggs.ViewModel.BasketViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ekspeace.eggs.Constants.Common.DELIVERY;
import static com.ekspeace.eggs.Constants.Common.currentDate;
import static com.ekspeace.eggs.Constants.Common.simpleDateFormat;

public class CheckOut extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = CheckOut.class.getSimpleName();
    private TextView tvItemsTotal, tvDelivery, tvTotal, tvNoItems;
    private RecyclerView recyclerView;
    private BasketViewModel basketViewModel;
    private double totalPrice = 0;
    private View layout;
    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> quantities = new ArrayList<>();
    private ArrayList<String> costs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Checkout.preload(getApplicationContext());

        basketViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(BasketViewModel.class);
        InitView();
        PopulateRecyclerView();
    }
    private void InitView(){
        tvItemsTotal = findViewById(R.id.checkout_items_total);
        tvDelivery = findViewById(R.id.checkout_delivery_service_price);
        tvTotal = findViewById(R.id.checkout_total);
        tvNoItems = findViewById(R.id.checkout_no_items);
        recyclerView = findViewById(R.id.checkout_recycler_view);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
        findViewById(R.id.checkout_button).setOnClickListener(view -> startPayment());
        findViewById(R.id.checkout_back).setOnClickListener(view -> onBackPressed());
    }
    private void PopulateRecyclerView(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        basketViewModel.getAllItems().observe(this, (Observer<List<Basket>>) baskets -> {
            if(baskets.isEmpty()){
                tvNoItems.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                tvNoItems.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            RecyclerViewItemsBasketAdapter adapter = new RecyclerViewItemsBasketAdapter(this, baskets, basketViewModel, layout);
            recyclerView.setAdapter(adapter);
            totalPrice = 0.0;
            for (Basket basket : baskets){
                totalPrice += basket.getEggPrice();
                items.add(basket.getEggName());
                costs.add(String.valueOf(basket.getEggPrice()));
                quantities.add(String.valueOf(basket.getEggQuantity()));
            }
            UpdatePrice();
        });
    }
    private void UpdatePrice(){
        tvItemsTotal.setText(String.format("%.2f", totalPrice));
        tvDelivery.setText(String.format("%.2f", DELIVERY));
        tvTotal.setText(String.format("%.2f", totalPrice+DELIVERY));
    }
    public void startPayment() {
        StoreOrder();
        EventBus.getDefault().unregister(this);
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_de2VlALc6McGDY");
        int amount = Math.round((float)(totalPrice+DELIVERY) * 100);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Eggs Corp");
            options.put("description", "Egg Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "ZAR");
            options.put("amount",  amount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", Common.currentUser.getEmail());
            preFill.put("contact", Common.currentUser.getPhone());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            PopUp.Toast(this, layout, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }
    private void StoreOrder(){
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(Common.currentUser.getId())
                .collection("Orders").document();
        HistoryOrder historyOrder = new HistoryOrder();
        if(!items.isEmpty())
            historyOrder.setItems(items);
        if (!quantities.isEmpty())
            historyOrder.setItemQuantity(quantities);
        if (!costs.isEmpty())
            historyOrder.setCost(costs);
        historyOrder.setArrived("No");
        historyOrder.setOrderDate(simpleDateFormat.format(currentDate.getTime()));
        documentReference.set(historyOrder).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                PopUp.Toast(CheckOut.this, layout, "Error: " + e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            PopUp.Toast(this, layout, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            PopUp.Toast(CheckOut.this, layout, "Error: " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onPaymentError(int code, String response) {
        try {
            PopUp.Toast(CheckOut.this, layout, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            PopUp.Toast(CheckOut.this, layout, "Error: " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void RemoveItem(DeleteEvent event) {
        if (event.isDeleted()) {
            basketViewModel.delete(event.getBasket());
        }
    }
}
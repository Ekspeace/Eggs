package com.ekspeace.eggs.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ekspeace.eggs.Adapter.RecyclerViewHistoryOrderAdapter;
import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Interface.IHistoryLoadListener;
import com.ekspeace.eggs.Model.EventBus.DeleteEvent;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.HistoryOrder;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryFragment extends Fragment implements IHistoryLoadListener {
    private static HistoryFragment instance;
    private RecyclerView recyclerView;
    private IHistoryLoadListener iHistoryLoadListener;
    private View layout;
    private HistoryOrder historyOrder;
    public static HistoryFragment getInstance() {
        if (instance == null) {
            instance = new HistoryFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        InitView(view);
        LoadHistoryOrder();
        return view;
    }
    private void InitView(View view){
        recyclerView = view.findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setClipToPadding(false);
        iHistoryLoadListener = this;
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container));
    }
    private void LoadHistoryOrder(){
        EventBus.getDefault().postSticky(new LoadingEvent(true));
        if (Common.isOnline(getContext())) {
            CollectionReference reference = FirebaseFirestore.getInstance()
                    .collection("Users").document(Common.currentUser.getId()).collection("Orders");

            List<HistoryOrder> historyOrders = new ArrayList<>();

            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        historyOrder = queryDocumentSnapshot.toObject(HistoryOrder.class);
                                        historyOrders.add(historyOrder);
                                    }
                                    iHistoryLoadListener.onHistoryLoadSuccess(historyOrders);
                                }
                                EventBus.getDefault().postSticky(new LoadingEvent(false));
                            }
                        }
                    }).addOnFailureListener(e -> iHistoryLoadListener.onHistoryLoadFailed(e.getMessage()));

        } else {
            PopUp.Network(getContext(), "Connection", "Please check your internet connectivity and try again");
            EventBus.getDefault().postSticky(new LoadingEvent(false));
        }
    }
    @Override
    public void onHistoryLoadSuccess(List<HistoryOrder> historyOrders) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewHistoryOrderAdapter adapter = new RecyclerViewHistoryOrderAdapter(getActivity(), historyOrders);
        recyclerView.setAdapter(adapter);
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }

    @Override
    public void onHistoryLoadFailed(String message) {
        PopUp.Toast(getActivity(), layout,message, Toast.LENGTH_SHORT);
        EventBus.getDefault().postSticky(new LoadingEvent(false));
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

        }
    }
}
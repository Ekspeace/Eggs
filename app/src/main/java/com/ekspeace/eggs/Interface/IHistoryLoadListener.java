package com.ekspeace.eggs.Interface;

import com.ekspeace.eggs.Model.HistoryOrder;
import com.ekspeace.eggs.Model.Recipe;

import java.util.List;

public interface IHistoryLoadListener {
    void onHistoryLoadSuccess(List<HistoryOrder> historyOrders);
    void onHistoryLoadFailed(String message);
}

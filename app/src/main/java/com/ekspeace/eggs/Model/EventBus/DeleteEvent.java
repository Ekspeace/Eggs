package com.ekspeace.eggs.Model.EventBus;


import com.ekspeace.eggs.Model.Basket;

public class DeleteEvent {

    private boolean isDeleted;
    private Basket basket;

    public DeleteEvent(boolean isDeleted, Basket basket) {
        this.isDeleted = isDeleted;
        this.basket = basket;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }
}

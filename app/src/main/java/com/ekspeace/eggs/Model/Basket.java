package com.ekspeace.eggs.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DecimalFormat;

@Entity(tableName = "basket_table")
public class Basket {
    private String eggName;
    private int eggQuantity;
    private String eggSize;
    private double eggPrice;
    private String eggImage;
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int Id;

    public Basket(int id) {
        Id = id;
    }

    public Basket(String eggName, int eggQuantity, String eggSize, double eggPrice, String eggImage) {
        this.eggName = eggName;
        this.eggQuantity = eggQuantity;
        this.eggSize = eggSize;
        this.eggPrice = eggPrice;
        this.eggImage = eggImage;
    }

    public String getEggName() {
        return eggName;
    }

    public void setEggName(String eggName) {
        this.eggName = eggName;
    }

    public int getEggQuantity() {
        return eggQuantity;
    }

    public void setEggQuantity(int eggQuantity) {
        this.eggQuantity = eggQuantity;
    }

    public String getEggSize() {
        return eggSize;
    }

    public void setEggSize(String eggSize) {
        this.eggSize = eggSize;
    }

    public double getEggPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(eggPrice));
    }

    public void setEggPrice(double eggPrice) {
        this.eggPrice = eggPrice;
    }

    public String getEggImage() {
        return eggImage;
    }

    public void setEggImage(String eggImage) {
        this.eggImage = eggImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}


package com.sillybubbles.game;

/**
 * Created by Timmy on 11/20/2015.
 */

public class PrizeItem {

    String itemName;
    int itemCount;

    //hidden constructor
    public PrizeItem() {

    }

    // constructor
    public PrizeItem(String name, int count) {
        this.itemName = name;
        this.itemCount = count;
    }

    // getters and setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String name) {
        this.itemName = name;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int count) {
        this.itemCount = count;
    }

}



package com.sillybubbles.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;

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


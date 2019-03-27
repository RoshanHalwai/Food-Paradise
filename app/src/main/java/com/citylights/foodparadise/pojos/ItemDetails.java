package com.citylights.foodparadise.pojos;

import java.io.Serializable;
import java.util.Map;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public class ItemDetails implements Serializable {

    private String category;
    private String itemDesc;
    private String itemId;
    private String itemName;
    private String itemPrice;
    private Integer quantity;
    private boolean veg;

    public ItemDetails() {
    }

    public ItemDetails(Map<String, Object> map) {
        this.category = (String) map.get("category");
        this.itemDesc = (String) map.get("itemDesc");
        this.itemId = (String) map.get("itemId");
        this.itemName = (String) map.get("itemName");
        this.itemPrice = (String) map.get("itemPrice");
        this.veg = (Boolean) map.get("veg");
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public boolean isVeg() {
        return veg;
    }

    public String getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}

package com.citylights.foodparadise.pojos;

import java.io.Serializable;
import java.util.Map;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public class VendorDetails implements Serializable {

    private String deliveryBoyName;
    private String deliveryBoyNumber;
    private String vendorId;
    private String vendorName;
    private String minimumOrderAmount;
    private Map<String, Object> menu;

    public VendorDetails() {
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public String getDeliveryBoyNumber() {
        return deliveryBoyNumber;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public Map<String, Object> getMenu() {
        return menu;
    }

}

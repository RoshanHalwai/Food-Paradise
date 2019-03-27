package com.citylights.foodparadise.pojos;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public class StationDetails implements Serializable {

    private String arrivalDate;
    private String arrivalTime;
    private String haltTime;
    private String stationCode;
    private String stationName;
    private List<Object> vendors;

    StationDetails() {
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getHaltTime() {
        return haltTime;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public List<Object> getVendors() {
        return vendors;
    }

    public List<String> getVendorCodes() {
        List<String> vendorCodesList = new LinkedList<>();
        if (vendors != null) {
            for (Object station : vendors) {
                vendorCodesList.add((String) station);
            }
        }
        return vendorCodesList;
    }
}

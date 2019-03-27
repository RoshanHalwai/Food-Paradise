package com.citylights.foodparadise.pojos;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * Created by Roshan Halwai on 3/25/2019
 */
public class OrderDetails implements Serializable {

    private List<ItemDetails> items;
    private String orderId;
    private String otp;
    private String passengerName;
    private String passengerMobileNumber;
    private String paymentMode;
    private String pnr;
    private String stationCode;
    private String vendorId;

    public OrderDetails() {
    }

    public OrderDetails(List<ItemDetails> items, String orderId, String passengerName, String passengerMobileNumber, String pnr, String stationCode, String vendorId) {
        this.items = items;
        this.orderId = orderId;
        this.otp = generateOTP();
        this.passengerName = passengerName;
        this.passengerMobileNumber = passengerMobileNumber;
        this.paymentMode = "Cash on Delivery";
        this.pnr = pnr;
        this.stationCode = stationCode;
        this.vendorId = vendorId;
    }

    private String generateOTP() {
        String numbers = "0123456789";
        Random random = new Random();
        char[] otp = new char[4];
        for (int i = 0; i < 4; i++) {
            otp[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(otp);
    }

    public List<ItemDetails> getItems() {
        return items;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOtp() {
        return otp;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getPassengerMobileNumber() {
        return passengerMobileNumber;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getPnr() {
        return pnr;
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getVendorId() {
        return vendorId;
    }

}

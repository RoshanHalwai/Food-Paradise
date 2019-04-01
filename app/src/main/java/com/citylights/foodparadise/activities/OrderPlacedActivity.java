package com.citylights.foodparadise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.citylights.foodparadise.IFirebaseCallbacks;
import com.citylights.foodparadise.R;
import com.citylights.foodparadise.adapters.OrderedFoodItemsAdapter;
import com.citylights.foodparadise.pojos.ItemDetails;
import com.citylights.foodparadise.pojos.OrderDetails;
import com.citylights.foodparadise.pojos.VendorDetails;

import java.text.DecimalFormat;

import static android.view.View.GONE;
import static com.citylights.foodparadise.Constants.GST_INDIA;
import static com.citylights.foodparadise.Constants.MONTH_DAY_FORMAT;
import static com.citylights.foodparadise.Constants.ORDER_DETAILS;
import static com.citylights.foodparadise.Constants.VENDORS_LOGO_MAP;
import static com.citylights.foodparadise.Constants.getFormattedDate;
import static com.citylights.foodparadise.Constants.hideProgressDialog;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.Constants.showProgressDialog;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.selectedItemDetailsList;

public class OrderPlacedActivity extends BaseActivity implements View.OnClickListener {

    private OrderDetails orderDetails;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order_placed;
    }

    @Override
    protected String getActivityTitle() {
        String activityTitle = "Order #";
        Intent intent = getIntent();
        if (intent != null) {
            orderDetails = (OrderDetails) intent.getSerializableExtra(ORDER_DETAILS);
            if (orderDetails != null) {
                activityTitle = activityTitle.concat(orderDetails.getOrderId());
            }
        }
        return activityTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.backButton).setVisibility(GONE);
        TextView textPlaceAnotherOrder = findViewById(R.id.textPlaceAnotherOrder);
        textPlaceAnotherOrder.setTypeface(setLatoRegularFont(this));
        textPlaceAnotherOrder.setOnClickListener(this);

        if (orderDetails != null) {
            showProgressDialog(this, "Placing your order", getString(R.string.please_wait_a_moment));
            updateOTPSection((isOTPSectionUpdated, isNull) -> {
                if (isOTPSectionUpdated) {
                    OrderAndFoodDetailsHeaderSection((isOrderFoodHeaderSectionUpdated, isNull12) -> {
                        if (isOrderFoodHeaderSectionUpdated) {
                            updateOrderDetailsValueSection((isOrderDetailsValueSectionUpdated, isNull1) -> {
                                if (isOrderDetailsValueSectionUpdated) {
                                    updateVendorHeaderSection((isVendorHeaderSectionUpdated, isNull2) -> {
                                        if (isVendorHeaderSectionUpdated) {
                                            updateOrderedItemsView((isOrderedItemsViewUpdated, isNull3) -> {
                                                if (isOrderedItemsViewUpdated) {
                                                    updateTotalPrice((isTotalPriceUpdated, isNull4) -> {
                                                        if (isTotalPriceUpdated) {
                                                            hideProgressDialog();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void updateOTPSection(IFirebaseCallbacks.Callback<Boolean> updated) {
        TextView textOrderPlaced = findViewById(R.id.textOrderPlaced);
        TextView textOtpValue = findViewById(R.id.textOtpValue);

        textOrderPlaced.setTypeface(setLatoBoldFont(this));
        textOtpValue.setTypeface(setLatoBoldFont(this));

        String textOtpValueStr = "OTP: " + orderDetails.getOtp();
        textOtpValue.setText(textOtpValueStr);
        updated.onCallback(true, false);
    }

    private void OrderAndFoodDetailsHeaderSection(IFirebaseCallbacks.Callback<Boolean> updated) {
        TextView textOrderDetails = findViewById(R.id.textOrderDetails);
        TextView textFoodDetails = findViewById(R.id.textFoodDetails);

        textOrderDetails.setTypeface(setLatoBoldFont(this));
        textFoodDetails.setTypeface(setLatoBoldFont(this));
        updated.onCallback(true, false);
    }

    private void updateOrderDetailsValueSection(IFirebaseCallbacks.Callback<Boolean> updated) {
        MainActivity.getStationDetails(orderDetails.getStationCode(), (stationDetails, isNull) -> {
            TextView textPassengerDetails = findViewById(R.id.textPassengerDetails);
            TextView textPassengerName = findViewById(R.id.textPassengerName);
            TextView textPassengerMobileNumber = findViewById(R.id.textPassengerMobileNumber);
            TextView textDeliveryInformation = findViewById(R.id.textDeliveryInformation);
            TextView textStationDetails = findViewById(R.id.textStationDetails);
            TextView textTrainDetails = findViewById(R.id.textTrainDetails);
            findViewById(R.id.layoutPassengerDeliveryDetails).setBackgroundColor(getResources().getColor(android.R.color.white));
            findViewById(R.id.passengerDeliveryDetailsLine).setVisibility(GONE);

            textPassengerDetails.setTypeface(setLatoRegularFont(this));
            textPassengerName.setTypeface(setLatoRegularFont(this));
            textPassengerMobileNumber.setTypeface(setLatoRegularFont(this));
            textDeliveryInformation.setTypeface(setLatoRegularFont(this));
            textStationDetails.setTypeface(setLatoRegularFont(this));
            textTrainDetails.setTypeface(setLatoRegularFont(this));

            String arrivalTime = stationDetails.getArrivalTime();
            String arrivalDate = stationDetails.getArrivalDate();
            String stationCode = stationDetails.getStationCode();
            String formattedStationDetails = stationCode + " - " + arrivalTime + ", " + getFormattedDate(arrivalDate, MONTH_DAY_FORMAT);

            textStationDetails.setText(formattedStationDetails);
            textPassengerName.setText(orderDetails.getPassengerName());
            textPassengerMobileNumber.setText(orderDetails.getPassengerMobileNumber());

            MainActivity.getPNRDetails(orderDetails.getPnr(), false, (pnrDetails, isNull12) -> {
                String trainNumber = pnrDetails.getTrainNumber();
                String coachNumber = pnrDetails.getCoach();
                String seatNumber = pnrDetails.getSeat();
                MainActivity.getTrainDetails(trainNumber, (trainDetails, isNull1) -> {
                    String formattedTrainDetails = trainNumber + " - " + coachNumber + ", " + seatNumber;
                    textTrainDetails.setText(formattedTrainDetails);
                    updated.onCallback(true, false);
                });
            });
        });
    }

    private void updateVendorHeaderSection(IFirebaseCallbacks.Callback<Boolean> updated) {
        MainActivity.getVendorDetails(orderDetails.getVendorId(), (vendorDetails, isNull) -> {
            TextView textVendorName = findViewById(R.id.textVendorName);
            ImageView imageVendorLogo = findViewById(R.id.imageVendorLogo);
            textVendorName.setTypeface(setLatoRegularFont(this));
            textVendorName.setText(vendorDetails.getVendorName());
            int vendorLogoDrawable = VENDORS_LOGO_MAP.get(vendorDetails.getVendorId());
            imageVendorLogo.setImageDrawable(getResources().getDrawable(vendorLogoDrawable));
            updateDeliveryDetailsSection(vendorDetails, (result, isNull1) -> updated.onCallback(true, false));
        });
    }

    private void updateDeliveryDetailsSection(VendorDetails vendorDetails, IFirebaseCallbacks.Callback<Boolean> updated) {
        TextView textVendorDeliveryDetails = findViewById(R.id.textVendorDeliveryDetails);
        TextView textDeliveryBoyName = findViewById(R.id.textDeliveryBoyName);
        TextView textDeliveryBoyMobileNumber = findViewById(R.id.textDeliveryBoyMobileNumber);

        textVendorDeliveryDetails.setTypeface(setLatoRegularFont(this));
        textDeliveryBoyName.setTypeface(setLatoRegularFont(this));
        textDeliveryBoyMobileNumber.setTypeface(setLatoRegularFont(this));

        textDeliveryBoyName.setText(vendorDetails.getDeliveryBoyName());
        textDeliveryBoyMobileNumber.setText(vendorDetails.getDeliveryBoyNumber());
        updated.onCallback(true, false);
    }

    private void updateOrderedItemsView(IFirebaseCallbacks.Callback<Boolean> updated) {
        /*Getting Id of recycler view*/
        RecyclerView recyclerOrderedItems = findViewById(R.id.recyclerOrderedItems);
        OrderedFoodItemsAdapter orderedFoodItemsAdapter = new OrderedFoodItemsAdapter(this, orderDetails.getItems());
        recyclerOrderedItems.setHasFixedSize(true);
        recyclerOrderedItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrderedItems.setVisibility(View.VISIBLE);
        recyclerOrderedItems.setAdapter(orderedFoodItemsAdapter);
        recyclerOrderedItems.setNestedScrollingEnabled(false);
        recyclerOrderedItems.setFocusable(false);
        updated.onCallback(true, false);
    }

    private void updateTotalPrice(IFirebaseCallbacks.Callback<Boolean> updated) {
        int totalAmount = 0;
        for (ItemDetails itemDetails : orderDetails.getItems()) {
            totalAmount = totalAmount + (Integer.valueOf(itemDetails.getItemPrice()) * itemDetails.getQuantity());
        }
        final float GSTTotal = totalAmount * GST_INDIA;
        final int total = Math.round(totalAmount + GSTTotal);
        TextView textSubtotalValue = findViewById(R.id.textSubtotalValue);
        TextView textGSTValue = findViewById(R.id.textGSTValue);
        TextView textTotalValue = findViewById(R.id.textTotalValue);
        TextView textSubtotal = findViewById(R.id.textSubtotal);
        TextView textGST = findViewById(R.id.textGST);
        TextView textTotal = findViewById(R.id.textTotal);

        textSubtotal.setTypeface(setLatoRegularFont(this));
        textSubtotalValue.setTypeface(setLatoRegularFont(this));
        textGST.setTypeface(setLatoRegularFont(this));
        textGSTValue.setTypeface(setLatoRegularFont(this));
        textTotal.setTypeface(setLatoRegularFont(this));
        textTotalValue.setTypeface(setLatoRegularFont(this));

        String subtotal = "Rs. " + String.valueOf(totalAmount);
        String gstValue = "Rs. " + new DecimalFormat("##.##").format(GSTTotal);
        String totalValue = "Rs. " + String.valueOf(total);

        textSubtotalValue.setText(subtotal);
        textGSTValue.setText(gstValue);
        textTotalValue.setText(totalValue);
        updated.onCallback(true, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textPlaceAnotherOrder) {
            selectedItemDetailsList.clear();
            Intent orderFoodIntent = new Intent(this, MainActivity.class);
            orderFoodIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(orderFoodIntent);
            finish();
        }
    }
}

package com.citylights.foodparadise.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citylights.foodparadise.IFirebaseCallbacks;
import com.citylights.foodparadise.R;
import com.citylights.foodparadise.adapters.SelectedFoodItemsAdapter;
import com.citylights.foodparadise.pojos.OrderDetails;
import com.citylights.foodparadise.pojos.StationDetails;
import com.citylights.foodparadise.pojos.TrainDetails;
import com.citylights.foodparadise.pojos.VendorDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DecimalFormat;
import java.util.Map;

import static com.citylights.foodparadise.Constants.COACH_NUMBER;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_ORDERS;
import static com.citylights.foodparadise.Constants.GST_INDIA;
import static com.citylights.foodparadise.Constants.MOBILE_NUMBER;
import static com.citylights.foodparadise.Constants.MONTH_DAY_FORMAT;
import static com.citylights.foodparadise.Constants.ORDER_DETAILS;
import static com.citylights.foodparadise.Constants.PASSENGER_NAME;
import static com.citylights.foodparadise.Constants.PNR_NUMBER;
import static com.citylights.foodparadise.Constants.SEAT_NUMBER;
import static com.citylights.foodparadise.Constants.STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.VENDORS_LOGO_MAP;
import static com.citylights.foodparadise.Constants.VENDOR_DETAILS;
import static com.citylights.foodparadise.Constants.getFormattedDate;
import static com.citylights.foodparadise.Constants.hideProgressDialog;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.Constants.showProgressDialog;
import static com.citylights.foodparadise.FirebaseCallbacks.firebaseCallbacks;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.selectedItemDetailsList;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.totalAmount;

@SuppressLint("StaticFieldLeak")
public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener {

    public static TextView textSubtotalValue;
    public static TextView textGSTValue;
    public static TextView textTotalValue;

    private String passengerName;
    private String passengerMobileNumber;
    private String pnrNumber;
    private String coachNumber;
    private String seatNumber;
    private TrainDetails trainDetails;
    private StationDetails stationDetails;
    private VendorDetails vendorDetails;
    private Button buttonContinue;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.confirm_order);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
            passengerName = sharedPreferences.getString(PASSENGER_NAME, "");
            passengerMobileNumber = sharedPreferences.getString(MOBILE_NUMBER, "");
            pnrNumber = sharedPreferences.getString(PNR_NUMBER, "");
            coachNumber = sharedPreferences.getString(COACH_NUMBER, "");
            seatNumber = sharedPreferences.getString(SEAT_NUMBER, "");
            trainDetails = (TrainDetails) intent.getSerializableExtra(TRAIN_DETAILS);
            stationDetails = (StationDetails) intent.getSerializableExtra(STATION_DETAILS);
            vendorDetails = (VendorDetails) intent.getSerializableExtra(VENDOR_DETAILS);

            if (passengerName.isEmpty() || passengerMobileNumber.isEmpty() || pnrNumber.isEmpty() || coachNumber.isEmpty() || seatNumber.isEmpty() ||
                    selectedItemDetailsList == null || selectedItemDetailsList.isEmpty() || trainDetails == null || stationDetails == null || vendorDetails == null) {
                Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
            } else {
                updateHeaderViews();
                updateBody();
                updateFooterViews();
            }
        } else {
            Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
        }
    }

    private void updateBody() {
        updateTotalPrice();

        /*Getting Id of recycler view*/
        RecyclerView recyclerSelectedItems = findViewById(R.id.recyclerSelectedItems);
        SelectedFoodItemsAdapter selectedFoodItemsAdapter = new SelectedFoodItemsAdapter(this);
        recyclerSelectedItems.setHasFixedSize(true);
        recyclerSelectedItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerSelectedItems.setVisibility(View.VISIBLE);
        recyclerSelectedItems.setAdapter(selectedFoodItemsAdapter);
        recyclerSelectedItems.setNestedScrollingEnabled(false);
        recyclerSelectedItems.setFocusable(false);
    }

    private void updateTotalPrice() {
        final float GSTTotal = totalAmount * GST_INDIA;
        final int total = Math.round(totalAmount + GSTTotal);
        textSubtotalValue = findViewById(R.id.textSubtotalValue);
        textGSTValue = findViewById(R.id.textGSTValue);
        textTotalValue = findViewById(R.id.textTotalValue);
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
    }

    private void updateHeaderViews() {
        TextView textVendorName = findViewById(R.id.textVendorName);
        TextView textOrderBefore = findViewById(R.id.textOrderBefore);
        TextView textMinimumOrder = findViewById(R.id.textMinimumOrder);
        ImageView imageVendorLogo = findViewById(R.id.imageVendorLogo);

        textVendorName.setTypeface(setLatoBoldFont(this));
        textOrderBefore.setTypeface(setLatoRegularFont(this));
        textMinimumOrder.setTypeface(setLatoRegularFont(this));

        textVendorName.setText(vendorDetails.getVendorName());
        String minimumOrderAmount = vendorDetails.getMinimumOrderAmount();
        String formattedMinOrder = "Min. order amount: Rs. " + minimumOrderAmount;
        textVendorName.setText(vendorDetails.getVendorName());
        textMinimumOrder.setText(formattedMinOrder);

        int vendorLogoDrawable = VENDORS_LOGO_MAP.get(vendorDetails.getVendorId());
        imageVendorLogo.setImageDrawable(getResources().getDrawable(vendorLogoDrawable));
    }

    private void updateFooterViews() {
        TextView textPassengerDetails = findViewById(R.id.textPassengerDetails);
        TextView textPassengerName = findViewById(R.id.textPassengerName);
        TextView textPassengerMobileNumber = findViewById(R.id.textPassengerMobileNumber);
        TextView textDeliveryInformation = findViewById(R.id.textDeliveryInformation);
        TextView textStationDetails = findViewById(R.id.textStationDetails);
        TextView textTrainDetails = findViewById(R.id.textTrainDetails);
        buttonContinue = findViewById(R.id.buttonContinue);

        textPassengerDetails.setTypeface(setLatoRegularFont(this));
        textPassengerName.setTypeface(setLatoRegularFont(this));
        textPassengerMobileNumber.setTypeface(setLatoRegularFont(this));
        textDeliveryInformation.setTypeface(setLatoRegularFont(this));
        textStationDetails.setTypeface(setLatoRegularFont(this));
        textTrainDetails.setTypeface(setLatoRegularFont(this));

        String arrivalTime = stationDetails.getArrivalTime();
        String arrivalDate = stationDetails.getArrivalDate();
        String stationCode = stationDetails.getStationCode();
        String stationDetails = stationCode + " - " + arrivalTime + ", " + getFormattedDate(arrivalDate, MONTH_DAY_FORMAT);

        String trainNumber = trainDetails.getTrainNumber();
        String trainDetails = trainNumber + " - " + coachNumber + ", " + seatNumber;

        textPassengerName.setText(passengerName);
        textPassengerMobileNumber.setText(passengerMobileNumber);
        textStationDetails.setText(stationDetails);
        textTrainDetails.setText(trainDetails);

        buttonContinue.setOnClickListener(this);
    }

    public void refreshActivity() {
        selectedItemDetailsList.clear();
        Intent orderFoodIntent = new Intent(this, OrderFoodActivity.class);
        orderFoodIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        orderFoodIntent.putExtra(TRAIN_DETAILS, getIntent().getSerializableExtra(TRAIN_DETAILS));
        orderFoodIntent.putExtra(STATION_DETAILS, getIntent().getSerializableExtra(STATION_DETAILS));
        orderFoodIntent.putExtra(VENDOR_DETAILS, getIntent().getSerializableExtra(VENDOR_DETAILS));
        startActivity(orderFoodIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        refreshActivity();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonContinue) {
            storeOrderDetailsInFirebase();
        }
    }

    private void storeOrderDetailsInFirebase() {
        showProgressDialog(this, "Placing your order", getString(R.string.please_wait_a_moment));
        getOrderId((orderId, isNull) -> {
            OrderDetails orderDetails = new OrderDetails(selectedItemDetailsList, orderId, passengerName, passengerMobileNumber, pnrNumber, stationDetails.getStationCode(), vendorDetails.getVendorId());
            DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_ORDERS);
            ordersReference.child(orderId).setValue(orderDetails).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    hideProgressDialog();
                    Intent orderPlacedIntent = new Intent(ConfirmOrderActivity.this, OrderPlacedActivity.class);
                    trainDetails = (TrainDetails) orderPlacedIntent.getSerializableExtra(TRAIN_DETAILS);
                    stationDetails = (StationDetails) orderPlacedIntent.getSerializableExtra(STATION_DETAILS);
                    vendorDetails = (VendorDetails) orderPlacedIntent.getSerializableExtra(VENDOR_DETAILS);
                    orderPlacedIntent.putExtra(ORDER_DETAILS, orderDetails);
                    startActivity(orderPlacedIntent);
                } else {
                    hideProgressDialog();
                    Toast.makeText(ConfirmOrderActivity.this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    private void getOrderId(IFirebaseCallbacks.Callback<String> orderId) {
        DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_ORDERS);
        Query query = ordersReference.orderByKey().limitToLast(1);
        firebaseCallbacks.getSnapshotForReference(query, (result, isNull) -> {
            if (isNull) {
                orderId.onCallback("1001", false);
            } else {
                Map<String, Object> lastOrderMap = ((Map<String, Object>) result.getValue());
                if (lastOrderMap != null) {
                    String lastOrderId = lastOrderMap.entrySet().iterator().next().getKey();
                    Integer lastOrderIdInt = Integer.valueOf(lastOrderId);
                    lastOrderIdInt = lastOrderIdInt + 1;
                    orderId.onCallback(String.valueOf(lastOrderIdInt), false);
                }
            }
        });
    }

}

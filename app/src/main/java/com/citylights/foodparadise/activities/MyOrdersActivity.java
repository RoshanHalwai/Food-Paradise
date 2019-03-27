package com.citylights.foodparadise.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.citylights.foodparadise.IFirebaseCallbacks;
import com.citylights.foodparadise.R;
import com.citylights.foodparadise.adapters.MyOrdersAdapter;
import com.citylights.foodparadise.pojos.OrderDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_ORDERS;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_PASSENGERS;
import static com.citylights.foodparadise.Constants.hideProgressDialog;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.Constants.showProgressDialog;
import static com.citylights.foodparadise.FirebaseCallbacks.firebaseCallbacks;

public class MyOrdersActivity extends BaseActivity {

    private int count = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_orders;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.your_orders);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerPastOrders = findViewById(R.id.recyclerPastOrders);

        TextView textNoPastOrders = findViewById(R.id.textNoPastOrders);
        textNoPastOrders.setTypeface(setLatoRegularFont(this));

        showProgressDialog(this, "Getting past orders", getString(R.string.please_wait_a_moment));
        getPastOrderIds("8667226939", (orderIdsList, isNull) -> {
            if (isNull) {
                hideProgressDialog();
                recyclerPastOrders.setVisibility(GONE);
                textNoPastOrders.setVisibility(VISIBLE);
            } else {
                getAllOrderDetails(orderIdsList, (orderDetailsList, isNull1) -> {
                    if (!isNull1) {
                        MyOrdersAdapter myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this, orderDetailsList);
                        recyclerPastOrders.setHasFixedSize(true);
                        recyclerPastOrders.setLayoutManager(new LinearLayoutManager(this));
                        textNoPastOrders.setVisibility(GONE);
                        recyclerPastOrders.setVisibility(VISIBLE);
                        recyclerPastOrders.setAdapter(myOrdersAdapter);
                        hideProgressDialog();
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getPastOrderIds(String mobileNumber, IFirebaseCallbacks.Callback<List<String>> orderIdCallback) {
        DatabaseReference mobileNumberReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_PASSENGERS).child(mobileNumber);
        firebaseCallbacks.getSnapshotForReference(mobileNumberReference, (result, isNull) -> {
            if (isNull) {
                orderIdCallback.onCallback(null, true);
            } else {
                orderIdCallback.onCallback((List<String>) result.getValue(), false);
            }
        });
    }

    private void getAllOrderDetails(List<String> orderIdList, IFirebaseCallbacks.Callback<List<OrderDetails>> orderDetailsCallback) {
        List<OrderDetails> orderDetailsList = new LinkedList<>();
        for (String orderId : orderIdList) {
            getOrderDetails(orderId, (orderDetails, isNull) -> {
                count++;
                if (!isNull) {
                    orderDetailsList.add(orderDetails);
                }
                if (count == orderIdList.size()) {
                    count = 0;
                    orderDetailsCallback.onCallback(orderDetailsList, false);
                }
            });
        }
    }

    private void getOrderDetails(String orderId, IFirebaseCallbacks.Callback<OrderDetails> orderDetailsCallback) {
        DatabaseReference ordersReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_ORDERS).child(orderId);
        firebaseCallbacks.getValueForReference(ordersReference, OrderDetails.class, (result, isNull) -> {
            if (isNull) {
                orderDetailsCallback.onCallback(null, true);
            } else {
                orderDetailsCallback.onCallback((OrderDetails) result, false);
            }
        });
    }

}

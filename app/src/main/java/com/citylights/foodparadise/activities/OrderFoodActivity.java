package com.citylights.foodparadise.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.adapters.FoodCategoryAdapter;
import com.citylights.foodparadise.pojos.VendorDetails;

import static android.view.View.GONE;
import static com.citylights.foodparadise.Constants.STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.VENDORS_LOGO_MAP;
import static com.citylights.foodparadise.Constants.VENDOR_DETAILS;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.selectedItemDetailsList;

@SuppressLint("StaticFieldLeak")
public class OrderFoodActivity extends BaseActivity implements View.OnClickListener {


    public static LinearLayout selectedItemsFooterLayout;
    public static TextView textTotalPrice;
    public static TextView textGSTDesc;
    public static Button buttonContinue;
    public static int minimumOrderAmountInteger;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_order_food;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.order_food);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            findViewById(R.id.layoutVendorDetails).setBackgroundColor(getResources().getColor(R.color.colorLightAccent));
            findViewById(R.id.line).setVisibility(GONE);

            VendorDetails vendorDetails = (VendorDetails) intent.getSerializableExtra(VENDOR_DETAILS);
            TextView textVendorName = findViewById(R.id.textVendorName);
            TextView textOrderBefore = findViewById(R.id.textOrderBefore);
            TextView textMinimumOrder = findViewById(R.id.textMinimumOrder);
            ImageView imageVendorLogo = findViewById(R.id.imageVendorLogo);

            textGSTDesc = findViewById(R.id.textGSTDesc);
            selectedItemsFooterLayout = findViewById(R.id.selectedItemsFooterLayout);
            textTotalPrice = findViewById(R.id.textTotalPrice);
            buttonContinue = findViewById(R.id.buttonContinue);

            textTotalPrice.setTypeface(setLatoBoldFont(this));
            textVendorName.setTypeface(setLatoBoldFont(this));
            textOrderBefore.setTypeface(setLatoRegularFont(this));
            textMinimumOrder.setTypeface(setLatoRegularFont(this));
            textGSTDesc.setTypeface(setLatoRegularFont(this));

            String minimumOrderAmount = vendorDetails.getMinimumOrderAmount();
            minimumOrderAmountInteger = Integer.valueOf(minimumOrderAmount);
            String formattedMinOrder = "Min. order amount: Rs. " + minimumOrderAmount;
            textVendorName.setText(vendorDetails.getVendorName());
            textMinimumOrder.setText(formattedMinOrder);

            int vendorLogoDrawable = VENDORS_LOGO_MAP.get(vendorDetails.getVendorId());
            imageVendorLogo.setImageDrawable(getResources().getDrawable(vendorLogoDrawable));

            /*Getting Id of recycler view*/
            RecyclerView recyclerFoodCategories = findViewById(R.id.recyclerFoodCategories);
            FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(this, vendorDetails.getMenu());
            recyclerFoodCategories.setHasFixedSize(true);
            recyclerFoodCategories.setLayoutManager(new LinearLayoutManager(this));
            recyclerFoodCategories.setVisibility(View.VISIBLE);
            recyclerFoodCategories.setAdapter(foodCategoryAdapter);
            recyclerFoodCategories.setNestedScrollingEnabled(false);
            recyclerFoodCategories.setFocusable(false);

            buttonContinue.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedItemDetailsList.isEmpty()) {
            super.onBackPressed();
        } else {
            showConfirmDialog(getString(R.string.replace_cart_item), getString(R.string.cart_contains_items), () -> {
                selectedItemDetailsList.clear();
                super.onBackPressed();
            });
        }
    }

    /**
     * Shows message box with title, message and method to be executed when user
     * clicks on YES button
     *
     * @param title   - Title of the message
     * @param message - Body of the message
     * @param method  - Method to execute after click of OK button
     */
    public void showConfirmDialog(String title, String message, Runnable method) {
        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(this);
        alertNotifyGateDialog.setCancelable(false);
        alertNotifyGateDialog.setTitle(title);
        alertNotifyGateDialog.setMessage(message);
        alertNotifyGateDialog.setPositiveButton("YES", (dialog, which) -> {
            dialog.cancel();
            method.run();
        });
        alertNotifyGateDialog.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        if (!isFinishing())
            alertNotifyGateDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonContinue) {
            Intent confirmDetails = new Intent(this, DetailsConfirmationActivity.class);
            confirmDetails.putExtra(TRAIN_DETAILS, getIntent().getSerializableExtra(TRAIN_DETAILS));
            confirmDetails.putExtra(STATION_DETAILS, getIntent().getSerializableExtra(STATION_DETAILS));
            confirmDetails.putExtra(VENDOR_DETAILS, getIntent().getSerializableExtra(VENDOR_DETAILS));
            startActivity(confirmDetails);
        }
    }
}

package com.citylights.foodparadise.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.citylights.foodparadise.R;

import static android.widget.Toast.LENGTH_LONG;
import static com.citylights.foodparadise.Constants.COACH_NUMBER;
import static com.citylights.foodparadise.Constants.MOBILE_NUMBER;
import static com.citylights.foodparadise.Constants.MOBILE_NUMBER_LENGTH;
import static com.citylights.foodparadise.Constants.PASSENGER_NAME;
import static com.citylights.foodparadise.Constants.SEAT_NUMBER;
import static com.citylights.foodparadise.Constants.STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.VENDOR_DETAILS;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;

public class DetailsConfirmationActivity extends BaseActivity implements View.OnClickListener {

    private EditText editPassengerName;
    private EditText editMobileNumber;
    private EditText editCoachNumber;
    private EditText editSeatNumber;
    private SharedPreferences sharedPreferences;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_details_confirmation;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.details_confirmation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        TextView textPassengerName = findViewById(R.id.textPassengerName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCoachNumber = findViewById(R.id.textCoachNumber);
        TextView textSeatNumber = findViewById(R.id.textSeatNumber);
        TextView textPersonalInfo = findViewById(R.id.textPersonalInfo);
        editPassengerName = findViewById(R.id.editPassengerName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        editCoachNumber = findViewById(R.id.editCoachNumber);
        editSeatNumber = findViewById(R.id.editSeatNumber);
        Button saveAndContinue = findViewById(R.id.buttonSaveAndContinue);

        textPassengerName.setTypeface(setLatoRegularFont(this));
        textMobileNumber.setTypeface(setLatoRegularFont(this));
        textCoachNumber.setTypeface(setLatoRegularFont(this));
        textSeatNumber.setTypeface(setLatoRegularFont(this));
        editPassengerName.setTypeface(setLatoRegularFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
        editCoachNumber.setTypeface(setLatoRegularFont(this));
        editSeatNumber.setTypeface(setLatoRegularFont(this));
        textPersonalInfo.setTypeface(setLatoBoldFont(this));

        fillPassengerDetails();
        saveAndContinue.setOnClickListener(this);
    }

    private void fillPassengerDetails() {
        String passengerName = sharedPreferences.getString(PASSENGER_NAME, "");
        String mobileNumber = sharedPreferences.getString(MOBILE_NUMBER, "");
        String coachNumber = sharedPreferences.getString(COACH_NUMBER, "");
        String seatNumber = sharedPreferences.getString(SEAT_NUMBER, "");
        if (coachNumber.isEmpty() || seatNumber.isEmpty()) {
            Toast.makeText(this, getString(R.string.please_try_again), LENGTH_LONG).show();
            return;
        } else {
            editCoachNumber.setText(coachNumber);
            editSeatNumber.setText(seatNumber);
        }
        if (!passengerName.isEmpty() && !mobileNumber.isEmpty()) {
            editPassengerName.setText(passengerName);
            editMobileNumber.setText(mobileNumber);
            editPassengerName.setSelection(passengerName.length());
            editMobileNumber.setSelection(mobileNumber.length());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSaveAndContinue) {
            final String passengerName = editPassengerName.getText().toString();
            final String mobileNumber = editMobileNumber.getText().toString();
            if (allFieldsValid(passengerName, mobileNumber)) {
                sharedPreferences.edit().putString(PASSENGER_NAME, passengerName).apply();
                sharedPreferences.edit().putString(MOBILE_NUMBER, mobileNumber).apply();

                Intent confirmOrderIntent = new Intent(this, ConfirmOrderActivity.class);
                confirmOrderIntent.putExtra(TRAIN_DETAILS, getIntent().getSerializableExtra(TRAIN_DETAILS));
                confirmOrderIntent.putExtra(STATION_DETAILS, getIntent().getSerializableExtra(STATION_DETAILS));
                confirmOrderIntent.putExtra(VENDOR_DETAILS, getIntent().getSerializableExtra(VENDOR_DETAILS));
                startActivity(confirmOrderIntent);

            }
        }

    }

    private boolean allFieldsValid(String passengerName, String mobileNumber) {
        boolean isAllFieldsValid = true;
        if (passengerName.isEmpty()) {
            Toast.makeText(this, "Please enter passenger name", Toast.LENGTH_LONG).show();
            isAllFieldsValid = false;
        } else if (mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_LONG).show();
            isAllFieldsValid = false;
        } else if (mobileNumber.length() != MOBILE_NUMBER_LENGTH) {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_LONG).show();
            isAllFieldsValid = false;
        }
        return isAllFieldsValid;
    }

}

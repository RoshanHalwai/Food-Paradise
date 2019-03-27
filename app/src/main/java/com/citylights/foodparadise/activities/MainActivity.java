package com.citylights.foodparadise.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.citylights.foodparadise.IFirebaseCallbacks;
import com.citylights.foodparadise.R;
import com.citylights.foodparadise.pojos.PNRDetails;
import com.citylights.foodparadise.pojos.StationDetails;
import com.citylights.foodparadise.pojos.TrainDetails;
import com.citylights.foodparadise.pojos.VendorDetails;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static android.view.MotionEvent.ACTION_UP;
import static android.widget.Toast.LENGTH_LONG;
import static com.citylights.foodparadise.Constants.ALL_STATION_DETAILS;
import static com.citylights.foodparadise.Constants.COACH_NUMBER;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_PNR;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_STATIONS;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_TRAINS;
import static com.citylights.foodparadise.Constants.FIREBASE_CHILD_VENDORS;
import static com.citylights.foodparadise.Constants.PNR_NUMBER;
import static com.citylights.foodparadise.Constants.PNR_NUMBER_LENGTH;
import static com.citylights.foodparadise.Constants.SEAT_NUMBER;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.hideProgressDialog;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.Constants.showProgressDialog;
import static com.citylights.foodparadise.FirebaseCallbacks.firebaseCallbacks;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    private int count = 0;

    public static void getPNRDetails(final String pnrNumber, final boolean updateSharedPreferences, final IFirebaseCallbacks.Callback<PNRDetails> pnrCallback) {
        DatabaseReference pnrNumberReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_PNR).child(pnrNumber);
        firebaseCallbacks.getValueForReference(pnrNumberReference, PNRDetails.class, (result, isNull) -> {
            if (isNull) {
                pnrCallback.onCallback(null, true);
            } else {
                PNRDetails pnrDetails = (PNRDetails) result;
                if (updateSharedPreferences) {
                    sharedPreferences.edit().putString(COACH_NUMBER, pnrDetails.getCoach()).apply();
                    sharedPreferences.edit().putString(SEAT_NUMBER, pnrDetails.getSeat()).apply();
                }
                pnrCallback.onCallback(pnrDetails, false);
            }
        });
    }

    public static void getTrainDetails(final String trainNumber, final IFirebaseCallbacks.Callback<TrainDetails> trainDetailsCallback) {
        DatabaseReference trainNumberReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_TRAINS).child(trainNumber);
        firebaseCallbacks.getValueForReference(trainNumberReference, TrainDetails.class, (result, isNull) -> {
            if (isNull) {
                trainDetailsCallback.onCallback(null, true);
            } else {
                trainDetailsCallback.onCallback((TrainDetails) result, false);
            }
        });
    }

    public static void getStationDetails(final String stationCode, final IFirebaseCallbacks.Callback<StationDetails> stationDetailsCallback) {
        DatabaseReference stationCodeReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_STATIONS).child(stationCode);
        firebaseCallbacks.getValueForReference(stationCodeReference, StationDetails.class, (result, isNull) -> {
            if (isNull) {
                stationDetailsCallback.onCallback(null, true);
            } else {
                stationDetailsCallback.onCallback((StationDetails) result, false);
            }
        });
    }

    public static void getVendorDetails(String vendorCode, IFirebaseCallbacks.Callback<VendorDetails> vendorDetailsCallback) {
        DatabaseReference vendorCodeReference = FirebaseDatabase.getInstance().getReference(FIREBASE_CHILD_VENDORS).child(vendorCode);
        firebaseCallbacks.getValueForReference(vendorCodeReference, VendorDetails.class, (result, isNull) -> {
            if (isNull) {
                vendorDetailsCallback.onCallback(null, true);
            } else {
                vendorDetailsCallback.onCallback((VendorDetails) result, false);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            Log.d(getString(R.string.app_name), e.getMessage());
        }

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        final EditText editPNRNumber = findViewById(R.id.editPNRNumber);
        TextView textAppName = findViewById(R.id.textAppName);
        TextView textAppDesc = findViewById(R.id.textAppDesc);
        TextView termsAndConditions = findViewById(R.id.termsAndConditions);

        termsAndConditions.setText(getText(R.string.terms_conditions));
        textAppName.setTypeface(setLatoBoldFont(this));
        textAppDesc.setTypeface(setLatoRegularFont(this));
        termsAndConditions.setTypeface(setLatoRegularFont(this));

        /* ------------------------------------------------------------- *
         * Setting text change listener on edit text
         * ------------------------------------------------------------- */

        editPNRNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == PNR_NUMBER_LENGTH) {
                    editPNRNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow_primary, 0);
                } else {
                    editPNRNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow_black, 0);
                }
            }
        });

        /* ------------------------------------------------------------- *
         * Setting touch listener on right drawable of edit text
         * ------------------------------------------------------------- */

        editPNRNumber.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == ACTION_UP) {
                if (event.getRawX() >= (editPNRNumber.getRight() - editPNRNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    getPnrAndTrainDetails(editPNRNumber.getText().toString());
                }
            }
            return false;
        });

        /* ------------------------------------------------------------- *
         * Setting key listener on key KEYCODE_ENTER edit text
         * ------------------------------------------------------------- */

        editPNRNumber.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                getPnrAndTrainDetails(editPNRNumber.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void getPnrAndTrainDetails(final String pnrNumber) {
        if (pnrNumber.length() != PNR_NUMBER_LENGTH) {
            Toast.makeText(MainActivity.this, getString(R.string.pnr_digits_error), LENGTH_LONG).show();
        } else {
            showProgressDialog(MainActivity.this, getString(R.string.fetching_pnr_details), getString(R.string.please_wait_a_moment));
            getPNRDetails(pnrNumber, true, (pnrDetails, isNull) -> {
                if (pnrDetails == null) {
                    hideProgressDialog();
                    Toast.makeText(MainActivity.this, getString(R.string.pnr_not_found_error), LENGTH_LONG).show();
                } else {
                    getTrainDetails(pnrDetails.getTrainNumber(), (trainDetails, isNull12) -> {
                        if (trainDetails == null) {
                            hideProgressDialog();
                            Toast.makeText(MainActivity.this, getString(R.string.please_try_again), LENGTH_LONG).show();
                        } else {
                            getAllStationDetails(trainDetails.getStationCodes(), (allStationDetails, isNull1) -> {
                                if (allStationDetails == null || allStationDetails.isEmpty()) {
                                    hideProgressDialog();
                                    Toast.makeText(MainActivity.this, getString(R.string.please_try_again), LENGTH_LONG).show();
                                } else {
                                    hideProgressDialog();
                                    startStationsActivity(pnrNumber, trainDetails, allStationDetails);
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void startStationsActivity(String pnrNumber, TrainDetails trainDetails, List<StationDetails> allStationDetails) {
        sharedPreferences.edit().putString(PNR_NUMBER, pnrNumber).apply();
        Intent stationIntent = new Intent(MainActivity.this, StationsActivity.class);
        stationIntent.putExtra(TRAIN_DETAILS, trainDetails);
        stationIntent.putExtra(ALL_STATION_DETAILS, (Serializable) allStationDetails);
        startActivity(stationIntent);
    }

    private void getAllStationDetails(final List<String> stationCodesList, final IFirebaseCallbacks.Callback<List<StationDetails>> stationDetailsCallback) {
        final List<StationDetails> stationDetailsList = new LinkedList<>();
        final int stationCodesListSize = stationCodesList.size();
        for (String stationCode : stationCodesList) {
            getStationDetails(stationCode, (stationDetails, isNull) -> {
                count++;
                if (!isNull) {
                    stationDetailsList.add(stationDetails);
                }
                if (count == stationCodesListSize) {
                    count = 0;
                    stationDetailsCallback.onCallback(stationDetailsList, false);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

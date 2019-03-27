package com.citylights.foodparadise.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.adapters.StationsAdapter;
import com.citylights.foodparadise.pojos.StationDetails;
import com.citylights.foodparadise.pojos.TrainDetails;

import java.util.List;

import static com.citylights.foodparadise.Constants.ALL_STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;

public class StationsActivity extends BaseActivity {

    private List<StationDetails> stationDetailsList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_stations;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String getActivityTitle() {
        Intent intent = getIntent();
        if (getIntent() != null) {
            TrainDetails trainDetails = (TrainDetails) intent.getSerializableExtra(TRAIN_DETAILS);
            stationDetailsList = (List<StationDetails>) intent.getSerializableExtra(ALL_STATION_DETAILS);
            return trainDetails.getTrainName().toUpperCase();
        }
        return getString(R.string.app_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id of recycler view*/
        RecyclerView recyclerStationDetails = findViewById(R.id.recyclerStationDetails);
        TrainDetails trainDetails = (TrainDetails) getIntent().getSerializableExtra(TRAIN_DETAILS);
        StationsAdapter stationsAdapter = new StationsAdapter(this, stationDetailsList, trainDetails);
        recyclerStationDetails.setHasFixedSize(true);
        recyclerStationDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerStationDetails.setVisibility(View.VISIBLE);
        recyclerStationDetails.setAdapter(stationsAdapter);
        recyclerStationDetails.setNestedScrollingEnabled(false);
        recyclerStationDetails.setFocusable(false);
    }
}

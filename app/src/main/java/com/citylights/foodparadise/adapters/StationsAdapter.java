package com.citylights.foodparadise.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citylights.foodparadise.IFirebaseCallbacks;
import com.citylights.foodparadise.R;
import com.citylights.foodparadise.activities.MainActivity;
import com.citylights.foodparadise.pojos.StationDetails;
import com.citylights.foodparadise.pojos.TrainDetails;
import com.citylights.foodparadise.pojos.VendorDetails;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_LONG;
import static com.citylights.foodparadise.Constants.MONTH_DAY_FORMAT;
import static com.citylights.foodparadise.Constants.getFormattedDate;
import static com.citylights.foodparadise.Constants.hideProgressDialog;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.Constants.showProgressDialog;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationViewAdapter> {

    private LayoutInflater mInflater;
    private List<StationDetails> stationDetailsList;
    private TrainDetails trainDetails;
    private Context context;
    private int count = 0;

    public StationsAdapter(Context context, List<StationDetails> stationDetailsList, TrainDetails trainDetails) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.stationDetailsList = stationDetailsList;
        this.trainDetails = trainDetails;
    }

    @NonNull
    @Override
    public StationViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_station_details, parent, false);
        return new StationViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewAdapter holder, int position) {
        StationDetails stationDetails = stationDetailsList.get(position);
        String arrivalDate = stationDetails.getArrivalDate();
        String stationCode = stationDetails.getStationCode();
        String haltTime = stationDetails.getHaltTime();
        String stationName = stationDetails.getStationName();
        String arrivalTime = stationDetails.getArrivalTime();

        String formattedStationName = stationName.toUpperCase() + " " + "(" + stationCode + ")";
        String formattedArrivalTime = "Arrival - " + arrivalTime + ", " + getFormattedDate(arrivalDate, MONTH_DAY_FORMAT);
        String formattedHaltTime = "Halt - " + haltTime;

        holder.textStationName.setText(formattedStationName);
        holder.textArrivalDateTime.setText(formattedArrivalTime);
        holder.textHaltTime.setText(formattedHaltTime);
    }

    @Override
    public int getItemCount() {
        return stationDetailsList.size();
    }

    class StationViewAdapter extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView textStationName;
        private TextView textArrivalDateTime;
        private TextView textHaltTime;
        private ImageView imageExpandStation;

        StationViewAdapter(View itemView) {
            super(itemView);

            textStationName = itemView.findViewById(R.id.textStationName);
            textArrivalDateTime = itemView.findViewById(R.id.textArrivalDateTime);
            textHaltTime = itemView.findViewById(R.id.textHaltTime);
            imageExpandStation = itemView.findViewById(R.id.imageExpandStation);

            textStationName.setTypeface(setLatoBoldFont(context));
            textArrivalDateTime.setTypeface(setLatoRegularFont(context));
            textHaltTime.setTypeface(setLatoRegularFont(context));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*Getting Id of recycler view*/
            RecyclerView recyclerVendorDetails = v.findViewById(R.id.recyclerVendorDetails);
            if (recyclerVendorDetails.getVisibility() == VISIBLE) {
                recyclerVendorDetails.setVisibility(GONE);
                imageExpandStation.setImageDrawable(context.getResources().getDrawable((R.drawable.white_plus)));
            } else {
                showProgressDialog(context, context.getString(R.string.fetching_vendor_details), context.getString(R.string.please_wait_a_moment));
                imageExpandStation.setImageDrawable(context.getResources().getDrawable((R.drawable.white_minus)));
                StationDetails stationDetails = stationDetailsList.get(getAdapterPosition());
                getAllVendorDetails(stationDetails.getVendorCodes(), (vendorDetailsList, isNull) -> {
                    hideProgressDialog();
                    recyclerVendorDetails.setVisibility(VISIBLE);
                    recyclerVendorDetails.setHasFixedSize(true);
                    recyclerVendorDetails.setLayoutManager(new LinearLayoutManager(context));
                    recyclerVendorDetails.setNestedScrollingEnabled(false);
                    if (isNull) {
                        Toast.makeText(context, context.getString(R.string.please_try_again), LENGTH_LONG).show();
                    } else if (vendorDetailsList.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.no_vendors_available), LENGTH_LONG).show();
                        imageExpandStation.setImageDrawable(context.getResources().getDrawable((R.drawable.white_plus)));
                    } else {
                        recyclerVendorDetails.setAdapter(new VendorsAdapter(context, vendorDetailsList, stationDetails, trainDetails));
                    }
                });
            }
        }

        private void getAllVendorDetails(final List<String> vendorCodesList, final IFirebaseCallbacks.Callback<List<VendorDetails>> stationDetailsCallback) {
            final List<VendorDetails> vendorDetailsList = new LinkedList<>();
            final int vendorCodesListSize = vendorCodesList.size();
            if (vendorCodesList.isEmpty()) {
                stationDetailsCallback.onCallback(vendorDetailsList, false);
            } else {
                for (String vendorCode : vendorCodesList) {
                    MainActivity.getVendorDetails(vendorCode, (stationDetails, isNull) -> {
                        count++;
                        if (!isNull) {
                            vendorDetailsList.add(stationDetails);
                        }
                        if (count == vendorCodesListSize) {
                            count = 0;
                            stationDetailsCallback.onCallback(vendorDetailsList, false);
                        }
                    });
                }
            }
        }

    }

}

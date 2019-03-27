package com.citylights.foodparadise.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.activities.OrderFoodActivity;
import com.citylights.foodparadise.pojos.StationDetails;
import com.citylights.foodparadise.pojos.TrainDetails;
import com.citylights.foodparadise.pojos.VendorDetails;

import java.util.List;

import static android.view.View.GONE;
import static com.citylights.foodparadise.Constants.STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.VENDORS_LOGO_MAP;
import static com.citylights.foodparadise.Constants.VENDOR_DETAILS;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.VendorViewAdapter> {

    private LayoutInflater mInflater;
    private Context context;
    private List<VendorDetails> vendorDetailsList;
    private StationDetails stationDetails;
    private TrainDetails trainDetails;

    VendorsAdapter(Context context, List<VendorDetails> vendorDetailsList, StationDetails stationDetails, TrainDetails trainDetails) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.vendorDetailsList = vendorDetailsList;
        this.stationDetails = stationDetails;
        this.trainDetails = trainDetails;
    }

    @NonNull
    @Override
    public VendorViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_vendor_details, parent, false);
        return new VendorViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorViewAdapter holder, int position) {
        VendorDetails vendorDetails = vendorDetailsList.get(position);
        String vendorName = vendorDetails.getVendorName();
        String minimumOrderAmount = vendorDetails.getMinimumOrderAmount();
        String formattedMinOrder = "Min. order amount: Rs. " + minimumOrderAmount;

        holder.textVendorName.setText(vendorName);
        holder.textMinimumOrder.setText(formattedMinOrder);
        int drawable = VENDORS_LOGO_MAP.get(vendorDetails.getVendorId());
        holder.imageVendorLogo.setImageDrawable(context.getResources().getDrawable(drawable));
        if (position == vendorDetailsList.size() - 1) {
            holder.line.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return vendorDetailsList.size();
    }

    class VendorViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textVendorName;
        private TextView textOrderBefore;
        private TextView textMinimumOrder;
        private ImageView imageVendorLogo;
        private View line;

        VendorViewAdapter(View itemView) {
            super(itemView);
            textVendorName = itemView.findViewById(R.id.textVendorName);
            textOrderBefore = itemView.findViewById(R.id.textOrderBefore);
            textMinimumOrder = itemView.findViewById(R.id.textMinimumOrder);
            imageVendorLogo = itemView.findViewById(R.id.imageVendorLogo);
            line = itemView.findViewById(R.id.line);

            textVendorName.setTypeface(setLatoBoldFont(context));
            textOrderBefore.setTypeface(setLatoRegularFont(context));
            textMinimumOrder.setTypeface(setLatoRegularFont(context));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent orderFoodIntent = new Intent(context, OrderFoodActivity.class);
            orderFoodIntent.putExtra(STATION_DETAILS, stationDetails);
            orderFoodIntent.putExtra(TRAIN_DETAILS, trainDetails);
            orderFoodIntent.putExtra(VENDOR_DETAILS, vendorDetailsList.get(getAdapterPosition()));
            context.startActivity(orderFoodIntent);
        }
    }

}

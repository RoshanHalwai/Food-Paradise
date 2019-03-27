package com.citylights.foodparadise.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.activities.MainActivity;
import com.citylights.foodparadise.pojos.OrderDetails;

import java.util.List;

import static com.citylights.foodparadise.Constants.VENDORS_LOGO_MAP;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;

/**
 * Created by Roshan Halwai on 3/25/2019
 */
public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.PastOrderViewAdapter> {

    private List<OrderDetails> orderDetailsList;
    private Context context;
    private LayoutInflater mInflater;

    public MyOrdersAdapter(Context context, List<OrderDetails> orderDetailsList) {
        this.context = context;
        this.orderDetailsList = orderDetailsList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PastOrderViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_past_orders, parent, false);
        return new MyOrdersAdapter.PastOrderViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderViewAdapter holder, int position) {
        OrderDetails orderDetails = orderDetailsList.get(position);
        MainActivity.getPNRDetails(orderDetails.getPnr(), false, (pnrDetails, isNull) -> {
            String trainNumber = pnrDetails.getTrainNumber();
            MainActivity.getTrainDetails(trainNumber, (trainDetails, isNull1) -> {
                String trainName = trainDetails.getTrainName();
                String formattedTrainDetails = trainName + " " + "(" + trainNumber + ")";
                String orderNumber = "#" + orderDetails.getOrderId();

                MainActivity.getVendorDetails(orderDetails.getVendorId(), (vendorDetails, isNull2) -> {
                    String vendorName = vendorDetails.getVendorName();
                    String otpNumber = "OTP: " + orderDetails.getOtp();

                    holder.textOrderNumber.setText(orderNumber);
                    holder.textOtpValue.setText(otpNumber);
                    holder.textTrainDetails.setText(formattedTrainDetails);
                    holder.textVendorName.setText(vendorName);
                    int vendorLogoDrawable = VENDORS_LOGO_MAP.get(vendorDetails.getVendorId());
                    holder.imageVendorLogo.setImageDrawable(context.getResources().getDrawable(vendorLogoDrawable));
                });
            });
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    class PastOrderViewAdapter extends RecyclerView.ViewHolder {

        private TextView textTrainDetails;
        private TextView textOrderNumber;
        private TextView textVendorName;
        private TextView textOrderPlaced;
        private TextView textOtpValue;
        private ImageView imageVendorLogo;

        PastOrderViewAdapter(View itemView) {
            super(itemView);

            textTrainDetails = itemView.findViewById(R.id.textTrainDetails);
            textOrderNumber = itemView.findViewById(R.id.textOrderNumber);
            textVendorName = itemView.findViewById(R.id.textVendorName);
            textOrderPlaced = itemView.findViewById(R.id.textOrderPlaced);
            textOtpValue = itemView.findViewById(R.id.textOtpValue);
            imageVendorLogo = itemView.findViewById(R.id.imageVendorLogo);

            textTrainDetails.setTypeface(setLatoBoldFont(context));
            textOrderNumber.setTypeface(setLatoRegularFont(context));
            textVendorName.setTypeface(setLatoRegularFont(context));
            textOrderPlaced.setTypeface(setLatoRegularFont(context));
            textOtpValue.setTypeface(setLatoRegularFont(context));
        }
    }

}

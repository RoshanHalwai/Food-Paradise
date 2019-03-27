package com.citylights.foodparadise.adapters;

import android.app.Activity;
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
import com.citylights.foodparadise.pojos.ItemDetails;

import java.text.DecimalFormat;

import static android.view.View.GONE;
import static com.citylights.foodparadise.Constants.GST_INDIA;
import static com.citylights.foodparadise.Constants.STATION_DETAILS;
import static com.citylights.foodparadise.Constants.TRAIN_DETAILS;
import static com.citylights.foodparadise.Constants.VENDOR_DETAILS;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.activities.ConfirmOrderActivity.textGSTValue;
import static com.citylights.foodparadise.activities.ConfirmOrderActivity.textSubtotalValue;
import static com.citylights.foodparadise.activities.ConfirmOrderActivity.textTotalValue;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.selectedItemDetailsList;
import static com.citylights.foodparadise.adapters.FoodItemAdapter.totalAmount;

/**
 * Created by Roshan Halwai on 3/24/2019
 */
public class SelectedFoodItemsAdapter extends RecyclerView.Adapter<SelectedFoodItemsAdapter.ItemViewAdapter> {

    private Context context;
    private LayoutInflater mInflater;

    public SelectedFoodItemsAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SelectedFoodItemsAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_selected_food_items, parent, false);
        return new SelectedFoodItemsAdapter.ItemViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter holder, int position) {
        ItemDetails itemDetails = getItemByAdapterPosition(position);
        String itemPrice = itemDetails.getItemPrice();
        int itemPriceInteger = Integer.valueOf(itemPrice);
        int quantity = itemDetails.getQuantity();
        int perItemTotalPrice = itemPriceInteger * quantity;

        String formattedItemPrice = "Rs. " + itemPrice;
        String formattedPerItemTotalPrice = "Rs. " + perItemTotalPrice;

        holder.textItemName.setText(itemDetails.getItemName());
        holder.textItemPrice.setText(formattedItemPrice);
        holder.textItemCount.setText(String.valueOf(quantity));
        holder.textPerItemPrice.setText(formattedPerItemTotalPrice);
        if (!itemDetails.isVeg()) {
            holder.imageVeg.setImageDrawable(context.getResources().getDrawable(R.drawable.non_veg_icon));
        }
        if (position == selectedItemDetailsList.size() - 1) {
            holder.line.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return selectedItemDetailsList.size();
    }

    private ItemDetails getItemByAdapterPosition(int position) {
        return selectedItemDetailsList.get(position);
    }

    class ItemViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageVeg;
        private View line;
        private TextView textItemName;
        private TextView textItemPrice;
        private TextView textPerItemPrice;
        private TextView textMinus;
        private TextView textAdd;
        private TextView textItemCount;

        ItemViewAdapter(View itemView) {
            super(itemView);

            textItemName = itemView.findViewById(R.id.textItemName);
            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textPerItemPrice = itemView.findViewById(R.id.textPerItemPrice);
            textMinus = itemView.findViewById(R.id.textRemoveItem);
            textAdd = itemView.findViewById(R.id.textAddItem);
            textItemCount = itemView.findViewById(R.id.textItemCount);
            imageVeg = itemView.findViewById(R.id.imageVeg);
            line = itemView.findViewById(R.id.line);

            textItemName.setTypeface(setLatoBoldFont(context));
            textItemPrice.setTypeface(setLatoRegularFont(context));
            textPerItemPrice.setTypeface(setLatoRegularFont(context));

            textAdd.setOnClickListener(this);
            textMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            totalAmount = 0;
            if (v == textAdd) {
                int currentItemCount = Integer.valueOf(textItemCount.getText().toString());
                currentItemCount = currentItemCount + 1;
                textItemCount.setText(String.valueOf(currentItemCount));
                ItemDetails itemDetails = getItemByAdapterPosition(getAdapterPosition());
                itemDetails.setQuantity(currentItemCount);
                updatePerItemPrice(itemDetails, currentItemCount);
            } else {
                int currentItemCount = Integer.valueOf(textItemCount.getText().toString());
                currentItemCount = currentItemCount - 1;
                if (currentItemCount == 0) {
                    selectedItemDetailsList.remove(getItemByAdapterPosition(getAdapterPosition()));
                    notifyDataSetChanged();
                    if (selectedItemDetailsList.isEmpty()) {
                        navigateToOrderFoodActivity();
                    }
                } else {
                    textItemCount.setText(String.valueOf(currentItemCount));
                    ItemDetails itemDetails = getItemByAdapterPosition(getAdapterPosition());
                    itemDetails.setQuantity(currentItemCount);
                    updatePerItemPrice(itemDetails, currentItemCount);
                }
            }
            updateTotal();
        }

        private void updatePerItemPrice(ItemDetails itemDetails, int currentItemCount) {
            String itemPrice = itemDetails.getItemPrice();
            int itemPriceInteger = Integer.valueOf(itemPrice);
            int perItemTotalPrice = itemPriceInteger * currentItemCount;
            String formattedPerItemTotalPrice = "Rs. " + perItemTotalPrice;
            textPerItemPrice.setText(formattedPerItemTotalPrice);
        }

        private void updateTotal() {
            for (ItemDetails itemDetails : selectedItemDetailsList) {
                String itemPrice = itemDetails.getItemPrice();
                int itemPriceInteger = Integer.valueOf(itemPrice);
                int itemsUnderEachCategory = itemDetails.getQuantity();
                itemPriceInteger = itemPriceInteger * itemsUnderEachCategory;
                totalAmount = totalAmount + itemPriceInteger;
            }

            final float GSTTotal = totalAmount * GST_INDIA;
            final int total = Math.round(totalAmount + GSTTotal);

            String subtotal = "Rs. " + String.valueOf(totalAmount);
            String gstValue = "Rs. " + new DecimalFormat("##.##").format(GSTTotal);
            String totalValue = "Rs. " + String.valueOf(total);

            textSubtotalValue.setText(subtotal);
            textGSTValue.setText(gstValue);
            textTotalValue.setText(totalValue);
        }

        private void navigateToOrderFoodActivity() {
            Intent orderFoodIntent = new Intent(context, OrderFoodActivity.class);
            orderFoodIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            Intent activityIntent = ((Activity) context).getIntent();
            orderFoodIntent.putExtra(TRAIN_DETAILS, activityIntent.getSerializableExtra(TRAIN_DETAILS));
            orderFoodIntent.putExtra(STATION_DETAILS, activityIntent.getSerializableExtra(STATION_DETAILS));
            orderFoodIntent.putExtra(VENDOR_DETAILS, activityIntent.getSerializableExtra(VENDOR_DETAILS));
            context.startActivity(orderFoodIntent);
            ((Activity) context).finish();
        }

    }

}

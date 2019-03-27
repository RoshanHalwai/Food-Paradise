package com.citylights.foodparadise.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.pojos.ItemDetails;

import java.util.LinkedList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;
import static com.citylights.foodparadise.activities.OrderFoodActivity.buttonContinue;
import static com.citylights.foodparadise.activities.OrderFoodActivity.minimumOrderAmountInteger;
import static com.citylights.foodparadise.activities.OrderFoodActivity.selectedItemsFooterLayout;
import static com.citylights.foodparadise.activities.OrderFoodActivity.textGSTDesc;
import static com.citylights.foodparadise.activities.OrderFoodActivity.textTotalPrice;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ItemViewAdapter> {

    public static int totalAmount = 0;
    public static List<ItemDetails> selectedItemDetailsList = new LinkedList<>();
    private List<ItemDetails> itemDetailsList;
    private Context context;
    private LayoutInflater mInflater;

    FoodItemAdapter(Context context, List<ItemDetails> itemDetailsList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemDetailsList = itemDetailsList;
    }

    @NonNull
    @Override
    public ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_food_items, parent, false);
        return new FoodItemAdapter.ItemViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter holder, int position) {
        ItemDetails itemDetails = itemDetailsList.get(position);
        String itemPrice = itemDetails.getItemPrice();
        String formattedItemPrice = "Rs. " + itemPrice;
        holder.textItemName.setText(itemDetails.getItemName());
        holder.textItemPrice.setText(formattedItemPrice);
        holder.textItemDesc.setText(itemDetails.getItemDesc());
        if (!itemDetails.isVeg()) {
            holder.imageVeg.setImageDrawable(context.getResources().getDrawable(R.drawable.non_veg_icon));
        }
        if (position == itemDetailsList.size() - 1) {
            holder.line.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemDetailsList.size();
    }

    class ItemViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageVeg;
        private TextView textItemName;
        private TextView textItemPrice;
        private TextView textItemDesc;
        private TextView textMinus;
        private TextView textAdd;
        private TextView textItemCount;
        private Button buttonAdd;
        private View line;
        private LinearLayout layoutItemCounter;

        ItemViewAdapter(View itemView) {
            super(itemView);

            textItemName = itemView.findViewById(R.id.textItemName);
            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textItemDesc = itemView.findViewById(R.id.textItemDesc);
            textMinus = itemView.findViewById(R.id.textRemoveItem);
            textAdd = itemView.findViewById(R.id.textAddItem);
            textItemCount = itemView.findViewById(R.id.textItemCount);
            imageVeg = itemView.findViewById(R.id.imageVeg);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
            line = itemView.findViewById(R.id.line);
            layoutItemCounter = itemView.findViewById(R.id.layoutItemCounter);

            textItemName.setTypeface(setLatoBoldFont(context));
            textItemPrice.setTypeface(setLatoRegularFont(context));
            textItemDesc.setTypeface(setLatoRegularFont(context));
            buttonAdd.setTypeface(setLatoRegularFont(context));

            buttonAdd.setOnClickListener(this);
            textAdd.setOnClickListener(this);
            textMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == buttonAdd) {
                buttonAdd.setVisibility(GONE);
                layoutItemCounter.setVisibility(VISIBLE);
                ItemDetails itemDetails = getItemByAdapterPosition(getAdapterPosition());
                itemDetails.setQuantity(1);
                selectedItemDetailsList.add(itemDetails);
                /*selectedItemsMap.put(getItemByAdapterPosition(getAdapterPosition()), 1);*/
            } else if (v == textAdd) {
                int currentItemCount = Integer.valueOf(textItemCount.getText().toString());
                currentItemCount = currentItemCount + 1;
                textItemCount.setText(String.valueOf(currentItemCount));
                ItemDetails itemDetails = getItemByAdapterPosition(getAdapterPosition());
                itemDetails.setQuantity(currentItemCount);
            } else {
                int currentItemCount = Integer.valueOf(textItemCount.getText().toString());
                currentItemCount = currentItemCount - 1;
                if (currentItemCount == 0) {
                    layoutItemCounter.setVisibility(GONE);
                    buttonAdd.setVisibility(VISIBLE);
                    selectedItemDetailsList.remove(getItemByAdapterPosition(getAdapterPosition()));
                } else {
                    textItemCount.setText(String.valueOf(currentItemCount));
                    ItemDetails itemDetails = getItemByAdapterPosition(getAdapterPosition());
                    itemDetails.setQuantity(currentItemCount);
                }
            }
            updateViewsInFooter();
        }

        private ItemDetails getItemByAdapterPosition(int position) {
            return itemDetailsList.get(position);
        }

        private void updateViewsInFooter() {
            if (selectedItemDetailsList.isEmpty()) {
                selectedItemsFooterLayout.setVisibility(GONE);
            } else {
                totalAmount = 0;
                int totalItems = 0;
                for (ItemDetails itemDetails : selectedItemDetailsList) {
                    String itemPrice = itemDetails.getItemPrice();
                    int itemPriceInteger = Integer.valueOf(itemPrice);
                    /*int itemsUnderEachCategory = selectedItemsMap.get(itemDetails);*/
                    int itemsUnderEachCategory = itemDetails.getQuantity();
                    itemPriceInteger = itemPriceInteger * itemsUnderEachCategory;
                    totalAmount = totalAmount + itemPriceInteger;
                    totalItems = totalItems + itemsUnderEachCategory;
                }

                final String itemsStr = totalItems == 1 ? "item" : "items";

                if (totalAmount < minimumOrderAmountInteger) {
                    textGSTDesc.setText(context.getString(R.string.add_more_items));
                    buttonContinue.setEnabled(false);
                } else {
                    buttonContinue.setEnabled(true);
                    textGSTDesc.setText(context.getString(R.string.gst_desc));
                }

                String totalPrice = "Rs. " + totalAmount + " " + "(" + totalItems + " " + itemsStr + ")";
                textTotalPrice.setText(totalPrice);
                selectedItemsFooterLayout.setVisibility(VISIBLE);
            }
        }
    }
}

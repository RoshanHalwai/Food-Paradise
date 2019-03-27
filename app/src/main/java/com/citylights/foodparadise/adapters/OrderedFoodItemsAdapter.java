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
import com.citylights.foodparadise.pojos.ItemDetails;

import java.util.List;

import static android.view.View.GONE;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;
import static com.citylights.foodparadise.Constants.setLatoRegularFont;

/**
 * Created by Roshan Halwai on 3/24/2019
 */
public class OrderedFoodItemsAdapter extends RecyclerView.Adapter<OrderedFoodItemsAdapter.ItemViewAdapter> {

    private Context context;
    private List<ItemDetails> orderedItemsList;
    private LayoutInflater mInflater;

    public OrderedFoodItemsAdapter(Context context, List<ItemDetails> orderedItemsList) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.orderedItemsList = orderedItemsList;
    }

    @NonNull
    @Override
    public OrderedFoodItemsAdapter.ItemViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_ordered_food_items, parent, false);
        return new OrderedFoodItemsAdapter.ItemViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter holder, int position) {
        ItemDetails itemDetails = getItemByAdapterPosition(position);
        String itemPrice = itemDetails.getItemPrice();
        int itemPriceInteger = Integer.valueOf(itemPrice);
        int quantity = itemDetails.getQuantity();
        int perItemTotalPrice = itemPriceInteger * quantity;

        String formattedItemPrice = String.valueOf(quantity) + " x " + "Rs. " + itemPrice;
        String formattedPerItemTotalPrice = "Rs. " + perItemTotalPrice;

        holder.textItemName.setText(itemDetails.getItemName());
        holder.textItemPrice.setText(formattedItemPrice);
        holder.textPerItemPrice.setText(formattedPerItemTotalPrice);
        if (!itemDetails.isVeg()) {
            holder.imageVeg.setImageDrawable(context.getResources().getDrawable(R.drawable.non_veg_icon));
        }
        if (position == orderedItemsList.size() - 1) {
            holder.line.setVisibility(GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderedItemsList.size();
    }

    private ItemDetails getItemByAdapterPosition(int position) {
        return orderedItemsList.get(position);
    }

    class ItemViewAdapter extends RecyclerView.ViewHolder {

        private ImageView imageVeg;
        private View line;
        private TextView textItemName;
        private TextView textItemPrice;
        private TextView textPerItemPrice;

        ItemViewAdapter(View itemView) {
            super(itemView);

            textItemName = itemView.findViewById(R.id.textItemName);
            textItemPrice = itemView.findViewById(R.id.textItemPrice);
            textPerItemPrice = itemView.findViewById(R.id.textPerItemPrice);
            imageVeg = itemView.findViewById(R.id.imageVeg);
            line = itemView.findViewById(R.id.line);

            textItemName.setTypeface(setLatoBoldFont(context));
            textItemPrice.setTypeface(setLatoRegularFont(context));
            textPerItemPrice.setTypeface(setLatoRegularFont(context));
        }

    }

}

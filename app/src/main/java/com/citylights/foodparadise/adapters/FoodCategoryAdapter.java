package com.citylights.foodparadise.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.citylights.foodparadise.R;
import com.citylights.foodparadise.pojos.ItemDetails;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.citylights.foodparadise.Constants.setLatoBoldFont;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.CategoryViewAdapter> {

    private LayoutInflater mInflater;
    private Map<String, Object> menu;
    private List<String> foodCategoryList;
    private Context context;

    public FoodCategoryAdapter(Context context, Map<String, Object> menu) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.menu = menu;
        this.foodCategoryList = new LinkedList<>(menu.keySet());
    }

    @NonNull
    @Override
    public FoodCategoryAdapter.CategoryViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_food_categories, parent, false);
        return new FoodCategoryAdapter.CategoryViewAdapter(view);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull FoodCategoryAdapter.CategoryViewAdapter holder, int position) {
        String category = foodCategoryList.get(position);
        Map<String, Object> map = (Map<String, Object>) menu.get(category);
        String modifiedCategory = category + " " + "(" + String.valueOf(map.size()) + ")";
        holder.textCategory.setText(modifiedCategory);
    }

    @Override
    public int getItemCount() {
        return foodCategoryList.size();
    }

    class CategoryViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textCategory;
        private ImageView imageExpandStation;

        CategoryViewAdapter(View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textCategory);
            imageExpandStation = itemView.findViewById(R.id.imageExpandStation);

            textCategory.setTypeface(setLatoBoldFont(context));
            itemView.setOnClickListener(this);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onClick(View v) {
            /*Getting Id of recycler view*/
            RecyclerView recyclerCategoryItems = v.findViewById(R.id.recyclerCategoryItems);
            if (recyclerCategoryItems.getVisibility() == VISIBLE) {
                recyclerCategoryItems.setVisibility(GONE);
                imageExpandStation.setImageDrawable(context.getResources().getDrawable((R.drawable.white_plus)));
            } else {
                List<ItemDetails> itemDetailsList = new LinkedList<>();
                Map<String, Object> map = (Map<String, Object>) menu.get(foodCategoryList.get(getAdapterPosition()));
                for (Map.Entry<String, Object> nestedEntry : map.entrySet()) {
                    itemDetailsList.add(new ItemDetails((Map<String, Object>) nestedEntry.getValue()));
                }
                imageExpandStation.setImageDrawable(context.getResources().getDrawable((R.drawable.white_minus)));
                recyclerCategoryItems.setVisibility(VISIBLE);
                recyclerCategoryItems.setHasFixedSize(true);
                recyclerCategoryItems.setLayoutManager(new LinearLayoutManager(context));
                recyclerCategoryItems.setAdapter(new FoodItemAdapter(context, itemDetailsList));
                recyclerCategoryItems.setNestedScrollingEnabled(false);
            }
        }
    }

}

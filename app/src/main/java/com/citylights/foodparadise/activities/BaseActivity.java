package com.citylights.foodparadise.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.citylights.foodparadise.R;

import static com.citylights.foodparadise.Constants.setLatoBoldFont;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public abstract class BaseActivity extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Abstract Methods
     * ------------------------------------------------------------- */

    protected abstract int getLayoutResourceId();

    protected abstract String getActivityTitle();

    private void setActivityTitle(final String title) {
        TextView activityTitle = findViewById(R.id.textActivityTitle);
        activityTitle.setTypeface(setLatoBoldFont(this));
        activityTitle.setText(title);
        setBackButtonListener();
    }

    private void setBackButtonListener() {
        ImageView backButton = findViewById(R.id.backButton);
        if (backButton != null) {
            backButton.setOnClickListener(view -> onBackPressed());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setActivityTitle(getActivityTitle());
    }

}

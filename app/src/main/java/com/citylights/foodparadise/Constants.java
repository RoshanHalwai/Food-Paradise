package com.citylights.foodparadise;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**

 * Created by Roshan Halwai on 3/22/2019
 */
public abstract class Constants {

    public static final float GST_INDIA = 0.05f;
    public static final Map<String, Integer> VENDORS_LOGO_MAP = new HashMap<String, Integer>() {{
        put("1001", R.drawable.dominos);
        put("1002", R.drawable.palymyra);
        put("1003", R.drawable.ananda_bhavan);
    }};

    /* ------------------------------------------------------------- *
     * Shared Preferences
     * ------------------------------------------------------------- */

    public static final String PASSENGER_NAME = "passengerName";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String COACH_NUMBER = "coachNumber";
    public static final String SEAT_NUMBER = "seatNumber";

    /* ------------------------------------------------------------- *

     * Intent Keys
     * ------------------------------------------------------------- */

    public static final String ALL_STATION_DETAILS = "all_station_details";
    public static final String PNR_NUMBER = "pnrNumber";
    public static final String STATION_DETAILS = "station_details";
    public static final String TRAIN_DETAILS = "train_details";
    public static final String VENDOR_DETAILS = "vendor_details";
    public static final String ORDER_DETAILS = "order_details";

    /* ------------------------------------------------------------- *
     * Date Formats
     * ------------------------------------------------------------- */

    public static final String MONTH_DAY_FORMAT = "MMM dd";
    private static final String SERVER_DATE_FORMAT = "ddMMyyyy";

    /* ------------------------------------------------------------- *
     * Locales
     * ------------------------------------------------------------- */

    private static final Locale LOCALE_INDIA = new Locale("en", "IN");

    /* ------------------------------------------------------------- *
     * Firebase Child
     * ------------------------------------------------------------- */

    public static final String FIREBASE_CHILD_PNR = "pnr";
    public static final String FIREBASE_CHILD_PASSENGERS = "passengers";
    public static final String FIREBASE_CHILD_STATIONS = "stations";
    public static final String FIREBASE_CHILD_TRAINS = "trains";
    public static final String FIREBASE_CHILD_VENDORS = "vendors";
    public static final String FIREBASE_CHILD_ORDERS = "orders";

    public static final int PNR_NUMBER_LENGTH = 10;
    public static final int MOBILE_NUMBER_LENGTH = PNR_NUMBER_LENGTH;

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String title, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public static String getFormattedDate(final String actualDateString, final String expectedFormat) {
        DateFormat sdf = new SimpleDateFormat(SERVER_DATE_FORMAT, LOCALE_INDIA);
        try {
            Date date = sdf.parse(actualDateString);
            return new SimpleDateFormat(expectedFormat, LOCALE_INDIA).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /* ------------------------------------------------------------- *
     * Font Types
     * ------------------------------------------------------------- */

    public static Typeface setLatoBoldFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Bold.ttf");
    }

    public static Typeface setLatoRegularFont(Context c) {
        return Typeface.createFromAsset(c.getAssets(), "fonts/Lato-Regular.ttf");
    }

}

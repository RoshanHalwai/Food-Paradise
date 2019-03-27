package com.citylights.foodparadise;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public interface IFirebaseCallbacks {

    void getValueForReference(DatabaseReference databaseReference, Class className, Callback<Object> resultValue);

    void getSnapshotForReference(Query query, Callback<DataSnapshot> snapshot);

    interface Callback<T> {
        void onCallback(T result, boolean isNull);
    }

}

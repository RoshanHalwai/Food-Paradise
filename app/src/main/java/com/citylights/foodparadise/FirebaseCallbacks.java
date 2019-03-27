package com.citylights.foodparadise;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Roshan Halwai on 3/22/2019
 */
public class FirebaseCallbacks implements IFirebaseCallbacks {

    public static FirebaseCallbacks firebaseCallbacks = getInstance();

    private static FirebaseCallbacks getInstance() {
        return new FirebaseCallbacks();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getValueForReference(DatabaseReference databaseReference, final Class className, final Callback<Object> resultValue) {
        getSnapshotForReference(databaseReference, (result, isNull) -> {
            if (isNull) {
                resultValue.onCallback(null, true);
            } else {
                resultValue.onCallback(result.getValue(className), false);
            }
        });
    }

    @Override
    public void getSnapshotForReference(Query query,
                                        final Callback<DataSnapshot> snapshotExists) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    snapshotExists.onCallback(dataSnapshot, false);
                } else {
                    snapshotExists.onCallback(null, true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                snapshotExists.onCallback(null, true);
            }
        });
    }

}

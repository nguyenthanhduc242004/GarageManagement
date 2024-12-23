package com.example.garagemanagement.Interfaces;

import com.google.firebase.firestore.DocumentSnapshot;

public interface FirestoreCallback {
    void onSuccess(DocumentSnapshot document);
    void onFailure(Exception e);
}

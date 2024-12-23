package com.example.garagemanagement;

import com.example.garagemanagement.Interfaces.FirestoreCallback;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirestoreHelper {
    private final FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public DocumentSnapshot getDocumentBasedOnFieldValueAwait(String collectionPath, String fieldName, String fieldValue) throws Exception {
        // Create a task to fetch data
        Task<QuerySnapshot> task = db.collection(collectionPath)
                .whereEqualTo(fieldName, fieldValue)
                .get();

        // Wait for the task to complete (blocking)
        QuerySnapshot querySnapshot = Tasks.await(task);

        // Check if any documents match
        if (!querySnapshot.isEmpty()) {
            return querySnapshot.getDocuments().get(0); // Return the first matching document
        }

        // Return null if no documents match
        return null;
    }

    public List<DocumentSnapshot> getDocumentBasedOnCollectionPath(String collectionPath) throws Exception {
        // Create a task to fetch data
        Task<QuerySnapshot> task = db.collection(collectionPath).get();

        // Wait for the task to complete (blocking)
        QuerySnapshot querySnapshot = Tasks.await(task);

        if (!querySnapshot.isEmpty()) {
            return querySnapshot.getDocuments();
        }

        // Return null if no documents match
        return null;
    }

    public void runTaskInBackground(String collectionPath, String fieldName, String fieldValue, FirestoreCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                DocumentSnapshot document = getDocumentBasedOnFieldValueAwait(collectionPath, fieldName, fieldValue);
                if (document != null) {
                    callback.onSuccess(document);
                } else {
                    callback.onSuccess(null);
                }
            } catch (Exception e) {
                callback.onFailure(e);
            } finally {
                executor.shutdown();
            }
        });
    }

    public void runTaskInBackground(String collectionPath, FirestoreCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                List<DocumentSnapshot> documents = getDocumentBasedOnCollectionPath(collectionPath);
                for (int i = 0; i < documents.size(); i++) {
                    try {
                        if (documents.get(i) != null) {
                            callback.onSuccess(documents.get(i));
                        } else {
                            callback.onSuccess(null);
                        }
                    } catch (Exception e) {
                        callback.onFailure(e);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdown();
            }
        });
    }
}
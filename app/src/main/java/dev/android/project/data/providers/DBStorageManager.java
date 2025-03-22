package dev.android.project.data.providers;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dev.android.project.data.providers.Firebase.FBStorage;

public class DBStorageManager
{
    private static final FirebaseStorage STORAGE = FBStorage.getInstance();
    private static final StorageReference _storageRef = STORAGE.getReference();

    public static void uploadProfilePicture(String userId, byte[] data)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");

        pfpRef.putBytes(data)
              .addOnSuccessListener(bytes -> Log.v("STORAGE", "Uploaded image successfully"))
              .addOnFailureListener(exception -> {
                  Log.v("STORAGE", "Failed to upload image");
                  Log.e("STORAGE", exception.getMessage());
              });
    }

    public static Task<byte[]> getProfilePicture(String userId)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");
        final long ONE_MEGABYTE = 1024 * 1024;

        return pfpRef.getBytes(ONE_MEGABYTE)
                     .addOnSuccessListener(
                             bytes -> Log.v("STORAGE", "Image retrieved successfully"))
                     .addOnFailureListener(
                             exception -> Log.w("STORAGE", "Failed to get image: " + exception.getMessage()));
    }

}

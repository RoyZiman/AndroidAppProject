package dev.android.project.data.providers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dev.android.project.data.providers.Firebase.FBStorage;

public class DBStorageManager
{
    private static final FirebaseStorage STORAGE = FBStorage.getInstance();
    private static final StorageReference _storageRef = STORAGE.getReference();
    private static final long ONE_MEGABYTE = 1024 * 1024;

    public static Task<Void> uploadProfilePicture(String userId, byte[] data)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        pfpRef.putBytes(data)
              .addOnSuccessListener(bytes -> taskCompletionSource.setResult(null))
              .addOnFailureListener(exception -> taskCompletionSource.setException(exception));

        return taskCompletionSource.getTask();
    }

    public static Task<Void> uploadProductPreview(String productId, byte[] data)
    {
        StorageReference previewRef = _storageRef.child("Products").child(productId).child("Preview.png");
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        previewRef.putBytes(data)
                  .addOnSuccessListener(bytes -> taskCompletionSource.setResult(null))
                  .addOnFailureListener(exception -> taskCompletionSource.setException(exception));

        return taskCompletionSource.getTask();
    }

    public static Task<Bitmap> getProfilePicture(String userId)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");
        return getImageFromStorage(pfpRef);
    }

    private static Task<Bitmap> getImageFromStorage(StorageReference ref)
    {
        TaskCompletionSource<Bitmap> taskCompletionSource = new TaskCompletionSource<>();

        ref.getBytes(ONE_MEGABYTE)
           .addOnSuccessListener(bytes -> taskCompletionSource.setResult(getImageFromBytes(bytes)))
           .addOnFailureListener(exception -> logFailedToGetImage(ref, exception));

        return taskCompletionSource.getTask();
    }

    private static Bitmap getImageFromBytes(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void logFailedToGetImage(StorageReference ref, Exception exception)
    {
        Log.w("DBStorage", "Failed to get image (" + ref.getPath() + "): " + exception.getMessage());
    }

    public static Task<Bitmap> getProductPreview(String productId)
    {
        {
            StorageReference imgRef = _storageRef.child("Products").child(productId).child("Preview.png");

            return getImageFromStorage(imgRef);
        }
    }


}

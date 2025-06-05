package dev.android.project.data.providers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import dev.android.project.data.providers.Firebase.FBStorage;

/**
 * Manages image storage operations in Firebase Storage.
 * <p>
 * Provides methods to upload and retrieve profile pictures and product previews.
 */
public class DBStorageManager
{
    private static final FirebaseStorage STORAGE = FBStorage.getInstance();
    private static final StorageReference _storageRef = STORAGE.getReference();
    private static final long ONE_MEGABYTE = 1024 * 1024;

    /**
     * Uploads a profile picture for a user to Firebase Storage.
     *
     * @param userId The ID of the user.
     * @param data The image data as a byte array.
     *
     * @return A {@link Task} representing the upload operation.
     */
    public static Task<Void> uploadProfilePicture(String userId, byte[] data)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        pfpRef.putBytes(data)
              .addOnSuccessListener(bytes -> taskCompletionSource.setResult(null))
              .addOnFailureListener(exception -> taskCompletionSource.setException(exception));

        return taskCompletionSource.getTask();
    }

    /**
     * Uploads a product preview image to Firebase Storage.
     *
     * @param productId The ID of the product.
     * @param data The image data as a byte array.
     *
     * @return A {@link Task} representing the upload operation.
     */
    public static Task<Void> uploadProductPreview(String productId, byte[] data)
    {
        StorageReference previewRef = _storageRef.child("Products").child(productId).child("Preview.png");
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        previewRef.putBytes(data)
                  .addOnSuccessListener(bytes -> taskCompletionSource.setResult(null))
                  .addOnFailureListener(exception -> taskCompletionSource.setException(exception));

        return taskCompletionSource.getTask();
    }

    /**
     * Retrieves a user's profile picture from Firebase Storage.
     *
     * @param userId The ID of the user.
     *
     * @return A {@link Task} containing the profile picture as a {@link Bitmap}.
     */
    public static Task<Bitmap> getProfilePicture(String userId)
    {
        StorageReference pfpRef = _storageRef.child("Users").child(userId).child("ProfilePicture.png");
        return getImageFromStorage(pfpRef);
    }

    /**
     * Retrieves an image from Firebase Storage as a {@link Bitmap}.
     *
     * @param ref The {@link StorageReference} to the image.
     *
     * @return A {@link Task} containing the image as a {@link Bitmap}.
     */
    private static Task<Bitmap> getImageFromStorage(StorageReference ref)
    {
        TaskCompletionSource<Bitmap> taskCompletionSource = new TaskCompletionSource<>();

        ref.getBytes(ONE_MEGABYTE)
           .addOnSuccessListener(bytes -> taskCompletionSource.setResult(getImageFromBytes(bytes)))
           .addOnFailureListener(exception -> logFailedToGetImage(ref, exception));

        return taskCompletionSource.getTask();
    }

    /**
     * Converts a byte array to a {@link Bitmap}.
     *
     * @param data The image data as a byte array.
     *
     * @return The decoded {@link Bitmap}.
     */
    private static Bitmap getImageFromBytes(byte[] data)
    {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * Logs a warning when failing to retrieve an image from Firebase Storage.
     *
     * @param ref The {@link StorageReference} to the image.
     * @param exception The exception that occurred.
     */
    private static void logFailedToGetImage(StorageReference ref, Exception exception)
    {
        Log.w("DBStorage", "Failed to get image (" + ref.getPath() + "): " + exception.getMessage());
    }

    /**
     * Retrieves a product preview image from Firebase Storage.
     *
     * @param productId The ID of the product.
     *
     * @return A {@link Task} containing the product preview as a {@link Bitmap}.
     */
    public static Task<Bitmap> getProductPreview(String productId)
    {
        StorageReference imgRef = _storageRef.child("Products").child(productId).child("Preview.png");
        return getImageFromStorage(imgRef);
    }
}

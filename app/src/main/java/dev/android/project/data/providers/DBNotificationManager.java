package dev.android.project.data.providers;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

import dev.android.project.data.models.Notification;
import dev.android.project.data.models.User;
import dev.android.project.data.providers.Firebase.FBFirestore;

/**
 * Manages notification-related operations in Firestore.
 * <p>
 * Provides methods to retrieve, send, and update notifications for users.
 */
public class DBNotificationManager
{
    public static final String COLLECTION_NAME = "notifications";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "content";
    public static final String FIELD_PRICE_OFFERED = "priceOffered";
    public static final String FIELD_IS_READ = "isRead";
    public static final String FIELD_SENDER_ID = "senderId";
    public static final String FIELD_RECEIVER_ID = "receiverId";
    public static final String FIELD_PRODUCT_ID = "productId";
    public static final String FIELD_TIMESTAMP = "timestamp";

    private static final CollectionReference _collectionRef = FBFirestore.getInstance().collection(COLLECTION_NAME);

    /**
     * Retrieves all notifications for the current user, ordered by timestamp descending.
     *
     * @return A {@link Task} containing a list of {@link Notification} objects.
     */
    public static Task<ArrayList<Notification>> getAllNotificationsForUser()
    {
        String userId = User.getCurrentUser().getId();

        TaskCompletionSource<ArrayList<Notification>> taskCompletionSource = new TaskCompletionSource<>();
        _collectionRef.whereEqualTo(FIELD_RECEIVER_ID, userId)
                      .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING).get()
                      .addOnCompleteListener(task -> {
                          if (task.isSuccessful())
                          {
                              ArrayList<Notification> notifications = new ArrayList<>();
                              for (DocumentSnapshot document : task.getResult().getDocuments())
                                  notifications.add(documentToNotification(document));

                              taskCompletionSource.setResult(notifications);
                          }
                          else
                              taskCompletionSource.setException(task.getException());
                      });
        return taskCompletionSource.getTask();
    }

    /**
     * Converts a Firestore {@link DocumentSnapshot} to a {@link Notification} object.
     *
     * @param document The Firestore document snapshot.
     *
     * @return The corresponding {@link Notification} object.
     */
    private static Notification documentToNotification(DocumentSnapshot document)
    {
        return new Notification(document.getString(FIELD_TITLE),
                                document.getString(FIELD_DESCRIPTION),
                                document.getDouble(FIELD_PRICE_OFFERED),
                                document.getBoolean(FIELD_IS_READ),
                                document.getString(FIELD_SENDER_ID),
                                document.getString(FIELD_RECEIVER_ID),
                                document.getString(FIELD_PRODUCT_ID),
                                document.getTimestamp(FIELD_TIMESTAMP))
                .setId(document.getId());
    }

    /**
     * Retrieves all unread notifications for the current user, ordered by timestamp descending.
     *
     * @return A {@link Task} containing a list of unread {@link Notification} objects.
     */
    public static Task<ArrayList<Notification>> getUnreadNotificationsForUser()
    {
        String userId = User.getCurrentUser().getId();

        TaskCompletionSource<ArrayList<Notification>> taskCompletionSource = new TaskCompletionSource<>();
        _collectionRef.whereEqualTo(FIELD_RECEIVER_ID, userId)
                      .whereEqualTo(FIELD_IS_READ, false)
                      .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING).get()
                      .addOnCompleteListener(task -> {
                          if (task.isSuccessful())
                          {
                              ArrayList<Notification> notifications = new ArrayList<>();
                              for (DocumentSnapshot document : task.getResult().getDocuments())
                                  notifications.add(documentToNotification(document));

                              taskCompletionSource.setResult(notifications);
                          }
                          else
                              taskCompletionSource.setException(task.getException());
                      });
        return taskCompletionSource.getTask();
    }

    /**
     * Sends a new notification by adding it to the Firestore collection.
     *
     * @param notification The {@link Notification} to send.
     *
     * @return A {@link Task} containing the sent {@link Notification} with its Firestore ID set.
     */
    public static Task<Notification> sendNotification(Notification notification)
    {
        TaskCompletionSource<Notification> taskCompletionSource = new TaskCompletionSource<>();
        _collectionRef.add(new HashMap<String, Object>()
        {{
            put(FIELD_TITLE, notification.getTitle());
            put(FIELD_DESCRIPTION, notification.getContent());
            put(FIELD_PRICE_OFFERED, notification.getPriceOffered());
            put(FIELD_IS_READ, notification.isRead());
            put(FIELD_SENDER_ID, notification.getSenderId());
            put(FIELD_RECEIVER_ID, notification.getReceiverId());
            put(FIELD_PRODUCT_ID, notification.getProductId());
            put(FIELD_TIMESTAMP, notification.getTimestamp());
        }}).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                notification.setId(task.getResult().getId());
                taskCompletionSource.setResult(notification);
            }
            else
            {
                taskCompletionSource.setException(task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }

    /**
     * Marks a notification as read in Firestore.
     *
     * @param notificationId The ID of the notification to mark as read.
     *
     * @return A {@link Task} representing the update operation.
     */
    public static Task<Void> setNotificationRead(String notificationId)
    {
        return _collectionRef.document(notificationId).update(FIELD_IS_READ, true);
    }

    /**
     * Retrieves a specific notification by its ID.
     *
     * @param notificationId The ID of the notification to retrieve.
     *
     * @return A {@link Task} containing the {@link Notification} object.
     */
    public static Task<Notification> getNotification(String notificationId)
    {
        TaskCompletionSource<Notification> taskCompletionSource = new TaskCompletionSource<>();
        _collectionRef.document(notificationId).get()
                      .addOnCompleteListener(task -> {
                          if (task.isSuccessful())
                          {
                              Notification notification = documentToNotification(task.getResult());
                              taskCompletionSource.setResult(notification);
                          }
                          else
                              taskCompletionSource.setException(task.getException());
                      });
        return taskCompletionSource.getTask();
    }
}

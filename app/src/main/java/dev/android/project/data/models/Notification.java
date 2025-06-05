package dev.android.project.data.models;

import com.google.firebase.Timestamp;

/**
 * Represents a notification sent between users, typically buy offers.
 */
public class Notification
{
    private final String _title;
    private final String _content;
    private final double _priceOffered;
    private final boolean _isRead;
    private final String _senderId;
    private final String _receiverId;
    private final String _productId;
    private final Timestamp _timestamp;
    private String _id;

    /**
     * Constructs a new Notification.
     *
     * @param title The title of the notification.
     * @param content The content or of the notification.
     * @param priceOffered The price offered.
     * @param isRead Whether the notification has been read.
     * @param senderId The ID of the user who sent the notification.
     * @param receiverId The ID of the user who receives the notification.
     * @param productId The ID of the related product.
     * @param timestamp The time the notification was created.
     */
    public Notification(String title,
                        String content, double priceOffered,
                        boolean isRead,
                        String senderId,
                        String receiverId,
                        String productId,
                        Timestamp timestamp)
    {
        _title = title;
        _content = content;
        _priceOffered = priceOffered;
        _isRead = isRead;
        _senderId = senderId;
        _receiverId = receiverId;
        _productId = productId;
        _timestamp = timestamp;
    }

    /**
     * Gets the notification's unique ID as stored in the database.
     *
     * @return The notification ID.
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Sets the notification's unique ID as stored in the database.
     *
     * @param id The notification ID.
     *
     * @return This Notification instance.
     */
    public Notification setId(String id)
    {
        _id = id;
        return this;
    }

    /**
     * Gets the title of the notification.
     *
     * @return The title.
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Gets the content of the notification.
     *
     * @return The content.
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Gets the price offered in the notification.
     *
     * @return The price offered.
     */
    public double getPriceOffered()
    {
        return _priceOffered;
    }

    /**
     * Checks if the notification has been read.
     *
     * @return True if read, false otherwise.
     */
    public boolean isRead()
    {
        return _isRead;
    }

    /**
     * Gets the sender's user ID.
     *
     * @return The sender's ID.
     */
    public String getSenderId()
    {
        return _senderId;
    }

    /**
     * Gets the receiver's user ID.
     *
     * @return The receiver's ID.
     */
    public String getReceiverId()
    {
        return _receiverId;
    }

    /**
     * Gets the related product's ID.
     *
     * @return The product ID.
     */
    public String getProductId()
    {
        return _productId;
    }

    /**
     * Gets the timestamp of when the notification was created.
     *
     * @return The timestamp.
     */
    public Timestamp getTimestamp()
    {
        return _timestamp;
    }
}
package dev.android.project.data.models;

import com.google.firebase.Timestamp;

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

    public Notification(String title,
                        String content, double priceOffered,
                        boolean isRead,
                        String senderId,
                        String receiverId,
                        String productId,
                        Timestamp timestamp)
    {

        // Todo: Force baked in text for title and content
        _title = title;
        _content = content;
        _priceOffered = priceOffered;
        _isRead = isRead;
        _senderId = senderId;
        _receiverId = receiverId;
        _productId = productId;
        _timestamp = timestamp;
    }

    public String getId()
    {
        return _id;
    }

    public Notification setId(String id)
    {
        _id = id;
        return this;
    }

    public String getTitle()
    {
        return _title;
    }

    public String getContent()
    {
        return _content;
    }

    public double getPriceOffered()
    {
        return _priceOffered;
    }

    public boolean isRead()
    {
        return _isRead;
    }

    public String getSenderId()
    {
        return _senderId;
    }

    public String getReceiverId()
    {
        return _receiverId;
    }

    public String getProductId()
    {
        return _productId;
    }

    public Timestamp getTimestamp()
    {
        return _timestamp;
    }
}

//public class OfferNotification extends Notification
//{
//    private final String _productId;
//
//
//    public OfferNotification(boolean isRead,
//                             String senderId,
//                             String receiverId,
//                             String productId,
//                             String productName,
//                             String senderName,
//                             double price)
//    {
//        super("New Offer",
//              String.format("%s Offers %.2f$ for %s", senderName, price, productName),
//              isRead,
//              senderId,
//              receiverId);
//        _productId = productId;
//    }
//
//}
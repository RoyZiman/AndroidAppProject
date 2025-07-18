package dev.android.project.ui.fragments.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.android.project.data.models.Notification;
import dev.android.project.data.providers.DBUsersManager;

public class NotificationViewModel extends ViewModel
{
    private final MutableLiveData<String> mNotificationTitle;
    private final MutableLiveData<String> mNotificationContent;
    private final MutableLiveData<String> mBuyerName;
    private final MutableLiveData<String> mPriceOffered;

    public NotificationViewModel()
    {
        mNotificationTitle = new MutableLiveData<>();
        mNotificationContent = new MutableLiveData<>();
        mBuyerName = new MutableLiveData<>();
        mPriceOffered = new MutableLiveData<>();

        mNotificationTitle.setValue("Notification Title");
        mNotificationContent.setValue("Notification Content");
        mBuyerName.setValue("Buyer Name");
        mPriceOffered.setValue("Price");
    }

    public MutableLiveData<String> getNotificationTitle()
    {
        return mNotificationTitle;
    }

    public MutableLiveData<String> getNotificationContent()
    {
        return mNotificationContent;
    }

    public MutableLiveData<String> getBuyerName()
    {
        return mBuyerName;
    }

    public MutableLiveData<String> getPriceOffered()
    {
        return mPriceOffered;
    }


    public void setNotification(Notification notification)
    {
        mNotificationTitle.setValue(notification.getTitle());
        mNotificationContent.setValue(notification.getContent());
        mPriceOffered.setValue(notification.getPriceOffered() + "$");

        DBUsersManager.getUser(notification.getSenderId()).addOnSuccessListener(
                user -> mBuyerName.setValue(user.getName()));

    }

}
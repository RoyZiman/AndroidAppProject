package dev.android.project.ui.createListing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateListingViewModel extends ViewModel
{

    private final MutableLiveData<String> mText;

    public CreateListingViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("This is create listing fragment");
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}
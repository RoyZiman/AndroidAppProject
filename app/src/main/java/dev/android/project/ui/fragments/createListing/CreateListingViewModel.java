package dev.android.project.ui.fragments.createListing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import dev.android.project.R;
import dev.android.project.data.model.Product;
import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.data.providers.DBStorageManager;

public class CreateListingViewModel extends ViewModel
{

    private final MutableLiveData<CreateListingFormState> _createListingFormState = new MutableLiveData<>();

    public CreateListingViewModel() {}

    LiveData<CreateListingFormState> getCreateListingFormState()
    {
        return _createListingFormState;
    }

    public Task<Product> Post(Product product, byte[] imageData)
    {
        return DBProductsManager.addProduct(product).addOnSuccessListener(uploadedProduct -> {
            DBStorageManager.uploadProductPreview(uploadedProduct.getId(), imageData);
        });

    }

    public void listingDataChanged(boolean isImageValid, String title, String description, String price)
    {
        if (!isTitleValid(title))
        {
            _createListingFormState.setValue(new CreateListingFormState(null, R.string.invalid_title, null, null));
        }
        else if (!isDescriptionValid(description))
        {
            _createListingFormState.setValue(
                    new CreateListingFormState(null, null, R.string.invalid_description, null));
        }
        else if (!isPriceValid(price))
        {
            _createListingFormState.setValue(new CreateListingFormState(null, null, null, R.string.invalid_price));
        }
        else if (!isImageValid)
        {
            _createListingFormState.setValue(new CreateListingFormState(R.string.invalid_image, null, null, null));
        }
        else
        {
            _createListingFormState.setValue(new CreateListingFormState(true));
        }
    }

    private boolean isTitleValid(String title)
    {
        return title != null && !title.isEmpty() && title.length() < 30;
    }

    private boolean isDescriptionValid(String description)
    {
        return description != null && !description.isEmpty() && description.length() < 500;
    }

    private boolean isPriceValid(String price)
    {
        return price != null && !price.isEmpty() && price.length() < 7;
    }

    public void imageDataChanged(boolean isImageValid)
    {
        if (!isImageValid)
            _createListingFormState.setValue(new CreateListingFormState(R.string.invalid_image, null, null, null));
        else
            _createListingFormState.setValue(new CreateListingFormState(true));
    }
}
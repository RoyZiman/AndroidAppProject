package dev.android.project.ui.fragments.createListing;

import androidx.annotation.Nullable;

public class CreateListingFormState
{

    @Nullable
    private final Integer _imageError;
    @Nullable
    private final Integer _titleError;
    @Nullable
    private final Integer _descriptionError;
    @Nullable
    private final Integer _priceError;

    private final boolean _isDataValid;

    CreateListingFormState(@Nullable Integer imageError,
                           @Nullable Integer titleError,
                           @Nullable Integer descriptionError,
                           @Nullable Integer priceError)
    {
        _imageError = imageError;
        _titleError = titleError;
        _descriptionError = descriptionError;
        _priceError = priceError;
        _isDataValid = false;
    }

    CreateListingFormState(boolean isDataValid)
    {
        _imageError = null;
        _titleError = null;
        _descriptionError = null;
        _priceError = null;
        _isDataValid = isDataValid;
    }

    @Nullable
    Integer getImageError()
    {
        return _imageError;
    }

    @Nullable
    Integer getTitleError()
    {
        return _titleError;
    }

    @Nullable
    Integer getDescriptionError()
    {
        return _descriptionError;
    }

    @Nullable
    Integer getPriceError()
    {
        return _priceError;
    }


    boolean isDataValid()
    {
        return _isDataValid;
    }
}

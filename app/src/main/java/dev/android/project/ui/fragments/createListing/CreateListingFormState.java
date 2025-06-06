package dev.android.project.ui.fragments.createListing;

import androidx.annotation.Nullable;

/**
 * Represents the state of the create listing form, including validation errors for each field.
 */
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

    /**
     * Constructs a new CreateListingFormState with specific error messages for each field.
     *
     * @param imageError Error message resource id for the image field, or null if no error.
     * @param titleError Error message resource id for the title field, or null if no error.
     * @param descriptionError Error message resource id for the description field, or null if no error.
     * @param priceError Error message resource id for the price field, or null if no error.
     */
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

    /**
     * Constructs a new CreateListingFormState with a validity flag and no errors.
     *
     * @param isDataValid True if the form data is valid, false otherwise.
     */
    CreateListingFormState(boolean isDataValid)
    {
        _imageError = null;
        _titleError = null;
        _descriptionError = null;
        _priceError = null;
        _isDataValid = isDataValid;
    }

    /**
     * @return the error message resource id for the image field, or null if no error.
     */
    @Nullable
    Integer getImageError()
    {
        return _imageError;
    }

    /**
     * @return the error message resource id for the title field, or null if no error.
     */
    @Nullable
    Integer getTitleError()
    {
        return _titleError;
    }

    /**
     * @return the error message resource id for the description field, or null if no error.
     */
    @Nullable
    Integer getDescriptionError()
    {
        return _descriptionError;
    }

    /**
     * @return the error message resource id for the price field, or null if no error.
     */
    @Nullable
    Integer getPriceError()
    {
        return _priceError;
    }

    /**
     * @return true if the form data is valid, false otherwise.
     */
    boolean isDataValid()
    {
        return _isDataValid;
    }
}

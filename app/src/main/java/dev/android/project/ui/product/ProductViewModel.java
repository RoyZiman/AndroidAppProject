package dev.android.project.ui.product;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.android.project.data.model.Product;

public class ProductViewModel extends ViewModel
{
    private final MutableLiveData<String> mProductTitle;
    private final MutableLiveData<String> mProductDescription;
    private final MutableLiveData<String> mProductPrice;


    public ProductViewModel()
    {
        mProductTitle = new MutableLiveData<>();
        mProductDescription = new MutableLiveData<>();
        mProductPrice = new MutableLiveData<>();

        mProductTitle.setValue("Product Title");
        mProductDescription.setValue("Product Description");
        mProductPrice.setValue("Product Price");
    }

    public MutableLiveData<String> getProductTitle()
    {
        return mProductTitle;
    }

    public MutableLiveData<String> getProductDescription()
    {
        return mProductDescription;
    }

    public MutableLiveData<String> getProductPrice()
    {
        return mProductPrice;
    }


    public void setProduct(Product product)
    {
        mProductTitle.setValue(product.getTitle());
        mProductDescription.setValue(product.getDescription());
        mProductPrice.setValue(product.getPriceAsString());
    }


}
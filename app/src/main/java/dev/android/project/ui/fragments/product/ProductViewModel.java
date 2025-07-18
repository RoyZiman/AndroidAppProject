package dev.android.project.ui.fragments.product;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dev.android.project.data.models.Product;

public class ProductViewModel extends ViewModel
{
    private final MutableLiveData<Bitmap> mProductPreview;
    private final MutableLiveData<String> mProductTitle;
    private final MutableLiveData<String> mProductDescription;
    private final MutableLiveData<String> mProductPrice;

    private final MutableLiveData<Product> mProduct;


    public ProductViewModel()
    {
        mProductPreview = new MutableLiveData<>();
        mProductTitle = new MutableLiveData<>();
        mProductDescription = new MutableLiveData<>();
        mProductPrice = new MutableLiveData<>();
        mProduct = new MutableLiveData<>();

        mProductTitle.setValue("Product Title");
        mProductDescription.setValue("Product Description");
        mProductPrice.setValue("Product Price");
    }

    public MutableLiveData<Bitmap> getProductPreview()
    {
        return mProductPreview;
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

    public Product getProduct()
    {
        return mProduct.getValue();
    }


    public void setProduct(Product product)
    {
        mProduct.setValue(product);

        mProductTitle.setValue(product.getTitle());
        mProductDescription.setValue(product.getDescription());
        mProductPrice.setValue(product.getPriceAsString());
    }

    public void setPreview(Bitmap bitmap)
    {
        mProductPreview.setValue(bitmap);
    }


}
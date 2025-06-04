package dev.android.project.ui.fragments.profile;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dev.android.project.data.models.Product;
import dev.android.project.data.models.User;
import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.data.providers.DBUsersManager;

public class ProfileViewModel extends ViewModel
{
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Product>> mProducts = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> mProfilePicture = new MutableLiveData<>();
    private final MutableLiveData<String> mError = new MutableLiveData<>();

    public LiveData<User> getUser()
    {
        return mUser;
    }

    public LiveData<Bitmap> getProfilePicture()
    {
        return mProfilePicture;
    }

    public LiveData<ArrayList<Product>> getProducts()
    {
        return mProducts;
    }

    public LiveData<String> getError()
    {
        return mError;
    }

    public void fetchUser()
    {
        if (User.isLoggedIn())
        {
            mUser.setValue(User.getCurrentUser());
            fetchUserProducts();
            fetchProfilePicture();
        }
    }

    private void fetchUserProducts()
    {
        DBProductsManager.getAllProductsByUser(mUser.getValue().getId()).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                mProducts.setValue(task.getResult());
            }
            else
            {
                mError.setValue(task.getException().getMessage());
            }
        });
    }

    private void fetchProfilePicture()
    {
        DBStorageManager.getProfilePicture(mUser.getValue().getId())
                        .addOnSuccessListener(bitmap -> mProfilePicture.setValue(bitmap))
                        .addOnFailureListener(e -> mError.setValue(e.getMessage()));
    }

    public void fetchUser(String userId)
    {
        DBUsersManager.getUser(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                mUser.setValue(task.getResult());
                fetchUserProducts();
                fetchProfilePicture();
            }
            else
            {
                mError.setValue(task.getException().getMessage());
            }
        });
    }
}
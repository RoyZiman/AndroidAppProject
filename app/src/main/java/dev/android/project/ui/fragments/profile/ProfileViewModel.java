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
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Product>> _products = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> _profilePicture = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    public LiveData<User> getUser()
    {
        return _user;
    }

    public LiveData<Bitmap> getProfilePicture()
    {
        return _profilePicture;
    }

    public LiveData<ArrayList<Product>> getProducts()
    {
        return _products;
    }

    public LiveData<String> getError()
    {
        return _error;
    }

    public void fetchUser()
    {
        if (User.isLoggedIn())
        {
            _user.setValue(User.getCurrentUser());
            fetchUserProducts();
            fetchProfilePicture();
        }
    }

    private void fetchUserProducts()
    {
        DBProductsManager.getAllProductsByUser(_user.getValue().getId()).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                _products.setValue(task.getResult());
            }
            else
            {
                _error.setValue(task.getException().getMessage());
            }
        });
    }

    private void fetchProfilePicture()
    {
        DBStorageManager.getProfilePicture(_user.getValue().getId())
                        .addOnSuccessListener(bitmap -> _profilePicture.setValue(bitmap))
                        .addOnFailureListener(e -> _error.setValue(e.getMessage()));
    }

    public void fetchUser(String userId)
    {
        DBUsersManager.getUser(userId).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                _user.setValue(task.getResult());
                fetchUserProducts();
                fetchProfilePicture();
            }
            else
            {
                _error.setValue(task.getException().getMessage());
            }
        });
    }
}
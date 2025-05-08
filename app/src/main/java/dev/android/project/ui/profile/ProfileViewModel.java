package dev.android.project.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dev.android.project.data.model.Product;
import dev.android.project.data.model.User;
import dev.android.project.data.providers.DBProductsManager;

public class ProfileViewModel extends ViewModel
{
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Product>> _products = new MutableLiveData<>();
    private final MutableLiveData<String> _error = new MutableLiveData<>();

    public LiveData<User> getUser()
    {
        return _user;
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
        }
    }

    public void fetchUserProducts()
    {
        if (User.isLoggedIn())
        {
            DBProductsManager.getAllProductsByUser(User.getCurrentUser().getId())
                             .addOnCompleteListener(task -> {
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
    }
}
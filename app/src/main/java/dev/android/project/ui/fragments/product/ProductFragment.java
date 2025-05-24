package dev.android.project.ui.fragments.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.Timestamp;

import dev.android.project.R;
import dev.android.project.data.model.Product;
import dev.android.project.data.model.User;
import dev.android.project.data.model.notifications.Notification;
import dev.android.project.data.providers.DBNotificationManager;
import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.data.providers.DBUsersManager;
import dev.android.project.databinding.FragmentProductBinding;

public class ProductFragment extends Fragment
{

    private FragmentProductBinding _binding;
    private ProductViewModel _productViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        _productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        _binding = FragmentProductBinding.inflate(inflater, container, false);

        if (getArguments() != null)
        {
            String productID = getArguments().getString("productID");
            setProduct(productID);
        }


        _productViewModel.getProductTitle()
                         .observe(getViewLifecycleOwner(), _binding.tvProductTitle::setText);
        _productViewModel.getProductDescription()
                         .observe(getViewLifecycleOwner(), _binding.tvProductDescription::setText);
        _productViewModel.getProductPrice()
                         .observe(getViewLifecycleOwner(), _binding.tvProductPrice::setText);

        _productViewModel.getProductPreview()
                         .observe(getViewLifecycleOwner(), bitmap -> {
                             if (bitmap != null)
                                 _binding.ivProductPreview.setImageBitmap(bitmap);
                         });


        _binding.btnSendOffer.setEnabled(false);

        _binding.etPriceOffer.addTextChangedListener(new android.text.TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                _binding.btnSendOffer.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });


        _binding.btnSendOffer.setOnClickListener(v -> {

            String offerPrice = _binding.etPriceOffer.getText()
                                                     .toString()
                                                     .trim();
            if (offerPrice.isEmpty())
            {
                Toast.makeText(getContext(), "Please enter a price", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = _productViewModel.getProduct();

            Notification notification = new Notification(
                    "New Buy Offer",
                    String.format("%s Offers %.2f$ for %s",
                                  User.getCurrentUser().getName(),
                                  Double.parseDouble(offerPrice),
                                  _productViewModel.getProductTitle().getValue()),
                    Double.parseDouble(offerPrice),
                    false,
                    User.getCurrentUser().getId(),
                    product.getStoreID(),
                    product.getId(),
                    new Timestamp(System.currentTimeMillis() / 1000, 0)
            );

            DBNotificationManager.sendNotification(notification).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    Navigation.findNavController(v).navigate(R.id.action_navProductView_to_navHome);
                else
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        return _binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _binding = null;
    }

    private void setProduct(String productID)
    {
        ProgressBar _loadingProgressBar = _binding.loading;
        _loadingProgressBar.setVisibility(View.VISIBLE);

        DBProductsManager.getProduct(productID).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                _productViewModel.setProduct(task.getResult());
                String sellerID = task.getResult().getStoreID();

                _binding.ivProfilePicture.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("userID", sellerID);
                    Navigation.findNavController(v).navigate(R.id.action_navProductView_to_navProfile, bundle);
                });

                // Fetch product preview
                DBStorageManager.getProductPreview(productID).addOnSuccessListener(bitmap -> {
                    _productViewModel.setPreview(bitmap);
                });

                // Fetch seller details
                DBUsersManager.getUser(sellerID).addOnCompleteListener(sellerTask -> {
                    if (sellerTask.isSuccessful())
                    {
                        User seller = sellerTask.getResult();
                        _binding.tvProductSeller.setText(seller.getName());
                    }
                    else
                        Toast.makeText(getContext(), sellerTask.getException().getMessage(), Toast.LENGTH_SHORT)
                             .show();
                });
                DBStorageManager.getProfilePicture(sellerID).addOnCompleteListener(sellerTask -> {
                    if (sellerTask.isSuccessful())
                    {
                        DBStorageManager.getProfilePicture(sellerID).addOnSuccessListener(
                                imageBitmap -> _binding.ivProfilePicture.setImageBitmap(imageBitmap));
                    }
                    else
                    {
                        Toast.makeText(getContext(), sellerTask.getException().getMessage(), Toast.LENGTH_SHORT)
                             .show();
                    }
                });
            }
            else
            {
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

            setLoggedInVisibility();
            _loadingProgressBar.setVisibility(View.GONE);
        });
    }

    private void setLoggedInVisibility()
    {
        if (User.isLoggedIn())
        {
            _binding.btnSendOffer.setVisibility(View.VISIBLE);
            _binding.etPriceOffer.setVisibility(View.VISIBLE);
            _binding.tvNotLoggedIn.setVisibility(View.GONE);
        }
        else
        {
            _binding.btnSendOffer.setVisibility(View.GONE);
            _binding.etPriceOffer.setVisibility(View.GONE);
            _binding.tvNotLoggedIn.setVisibility(View.VISIBLE);
        }
    }

}
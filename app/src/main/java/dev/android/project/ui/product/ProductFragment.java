package dev.android.project.ui.product;

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

import dev.android.project.data.model.User;
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
        View root = _binding.getRoot();

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

        return root;
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

                // Fetch product preview
                DBStorageManager.getProductPreview(productID).addOnSuccessListener(bitmap -> {
                    _productViewModel.setPreview(bitmap);
                });

                // Fetch seller details
                String sellerID = task.getResult().getStoreID(); // Assuming getSellerID() exists
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
                        DBStorageManager.getProfilePicture(sellerID).addOnSuccessListener(imageBitmap -> {
                            _binding.ivUserImage.setImageBitmap(imageBitmap);
                        });
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

            _loadingProgressBar.setVisibility(View.GONE);
        });
    }

}
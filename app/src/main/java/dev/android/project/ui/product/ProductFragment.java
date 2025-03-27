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

import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.databinding.FragmentProductBinding;

public class ProductFragment extends Fragment
{

    private FragmentProductBinding _binding;
    private ProductViewModel _productViewModel;


    public static ProductFragment newInstance()
    {
        return new ProductFragment();
    }

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
                _productViewModel.setProduct(task.getResult());
            else
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            _loadingProgressBar.setVisibility(View.GONE);
        });
    }

}
package dev.android.project.ui.createListing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dev.android.project.databinding.FragmentCreateListingBinding;

public class CreateListingFragment extends Fragment
{

    private FragmentCreateListingBinding _binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        CreateListingViewModel CreateListingViewModel =
                new ViewModelProvider(this).get(CreateListingViewModel.class);

        _binding = FragmentCreateListingBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();

        final TextView textView = _binding.tvCreateListing;
        CreateListingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _binding = null;
    }
}
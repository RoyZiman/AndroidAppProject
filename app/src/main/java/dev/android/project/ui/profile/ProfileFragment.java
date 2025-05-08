package dev.android.project.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dev.android.project.R;
import dev.android.project.databinding.FragmentProfileBinding;
import dev.android.project.ui.home.ProductRecyclerViewAdapter;

public class ProfileFragment extends Fragment
{
    private FragmentProfileBinding _binding;
    private ProfileViewModel _viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        _binding = FragmentProfileBinding.inflate(inflater, container, false);
        _viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        _viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null)
            {
                _binding.tvUserName.setText(user.getName());
                _binding.tvUserEmail.setText(user.getEmail());
            }
        });

        _viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            _binding.listUserProducts.setAdapter(
                    new ProductRecyclerViewAdapter(products, R.id.action_navProfile_to_navProductView));
            _binding.loading.setVisibility(View.GONE);
        });

        _viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null)
            {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                _binding.loading.setVisibility(View.GONE);
            }
        });

        _binding.listUserProducts.setLayoutManager(new LinearLayoutManager(getContext(),
                                                                           LinearLayoutManager.HORIZONTAL,
                                                                           false));
        _binding.loading.setVisibility(View.VISIBLE);

        if (_viewModel.getUser().getValue() == null)
        {
            _viewModel.fetchUser();
        }

        if (_viewModel.getProducts().getValue() == null)
        {
            _viewModel.fetchUserProducts();
        }

        _binding.btnUserListings.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("filterUserID", _viewModel.getUser().getValue().getId());
            Navigation.findNavController(v).navigate(R.id.action_navProfile_to_navHome, bundle);
        });

        return _binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _binding = null;
    }
}
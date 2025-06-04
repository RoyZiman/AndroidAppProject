package dev.android.project.ui.fragments.profile;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.android.project.R;
import dev.android.project.data.models.User;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.databinding.FragmentProfileBinding;
import dev.android.project.ui.fragments.home.ProductRecyclerViewAdapter;

public class ProfileFragment extends Fragment
{
    private static final int PICK_IMAGE_REQUEST = 1;
    private FragmentProfileBinding _binding;
    private ProfileViewModel _viewModel;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @NonNull Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri imageUri = data.getData();
            try (InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri))
            {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1)
                {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                byte[] imageData = byteArrayOutputStream.toByteArray();

                DBStorageManager.uploadProfilePicture(_viewModel.getUser().getValue().getId(), imageData)
                                .addOnSuccessListener(task -> {
                                    NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                                    View headerView = navigationView.getHeaderView(0);
                                    ImageView menuProfileImage = headerView.findViewById(R.id.ivProfilePicture);
                                    menuProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(imageData,
                                                                                                  0,
                                                                                                  imageData.length));

                                    _binding.ivProfilePicture.setImageBitmap(
                                            BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                                });


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

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


                if (User.isLoggedIn() && User.getCurrentUser().getId().equals(user.getId()))
                {
                    _binding.ivProfilePicture.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    });

                }
            }
        });

        _viewModel.getProfilePicture().observe(getViewLifecycleOwner(), bitmap -> {
            if (bitmap != null)
            {
                _binding.ivProfilePicture.setImageBitmap(bitmap);
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
            String userId = getArguments() != null ? getArguments().getString("userID") : null;
            if (userId == null)
                _viewModel.fetchUser();
            else
                _viewModel.fetchUser(userId);
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
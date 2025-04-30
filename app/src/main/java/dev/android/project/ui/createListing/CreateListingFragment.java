package dev.android.project.ui.createListing;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.android.project.data.model.User;
import dev.android.project.databinding.FragmentCreateListingBinding;

public class CreateListingFragment extends Fragment
{
    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean _isImageValid = false;
    private CreateListingViewModel _createListingViewModel;
    private FragmentCreateListingBinding _binding;

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

                _binding.ivProductPreview.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                _isImageValid = true;
                _createListingViewModel.imageDataChanged(_isImageValid);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        _binding = FragmentCreateListingBinding.inflate(inflater, container, false);
        View root = _binding.getRoot();


        _createListingViewModel = new ViewModelProvider(this).get(CreateListingViewModel.class);

        setFormVisibility();

        final FloatingActionButton fabAddImage = _binding.fabAddImage;
        final EditText etTitle = _binding.etTitle;
        final EditText etDescription = _binding.etDescription;
        final EditText etPrice = _binding.etPrice;
        final Button btnPost = _binding.btnPost;

        _createListingViewModel.getCreateListingFormState().observe(getViewLifecycleOwner(), createListingFormState ->
        {
            if (createListingFormState == null)
                return;

            btnPost.setEnabled(createListingFormState.isDataValid());

            if (createListingFormState.getTitleError() != null)
                etTitle.setError(getString(createListingFormState.getTitleError()));
            else if (createListingFormState.getDescriptionError() != null)
                etDescription.setError(getString(createListingFormState.getDescriptionError()));
            else if (createListingFormState.getPriceError() != null)
                etPrice.setError(getString(createListingFormState.getPriceError()));

        });

        TextWatcher afterTextChangedListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                _createListingViewModel.listingDataChanged(
                        _isImageValid,
                        etTitle.getText().toString(),
                        etDescription.getText().toString(),
                        etPrice.getText().toString());
            }
        };

        etTitle.addTextChangedListener(afterTextChangedListener);
        etDescription.addTextChangedListener(afterTextChangedListener);
        etPrice.addTextChangedListener(afterTextChangedListener);


        fabAddImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });


        return root;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _binding = null;
    }

    private void setFormVisibility()
    {

        Group groupCreateListing = _binding.groupCreateListing;
        TextView tvNotLoggedIn = _binding.tvNotLoggedIn;

        if (User.isLoggedIn())
        {
            groupCreateListing.setVisibility(View.VISIBLE);
            tvNotLoggedIn.setVisibility(View.GONE);
        }
        else
        {
            groupCreateListing.setVisibility(View.GONE);
            tvNotLoggedIn.setVisibility(View.VISIBLE);
        }
    }
}
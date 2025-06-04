package dev.android.project.ui.fragments.notification;

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

import dev.android.project.R;
import dev.android.project.data.providers.DBNotificationManager;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment
{
    private FragmentNotificationBinding _binding;
    private NotificationViewModel _notificationViewModel;

    public static NotificationFragment newInstance()
    {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        _notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        _binding = FragmentNotificationBinding.inflate(inflater, container, false);

        if (getArguments() != null)
        {
            String notificationID = getArguments().getString("notificationID");
            setNotification(notificationID);
        }

        _notificationViewModel.getNotificationTitle()
                              .observe(getViewLifecycleOwner(), _binding.tvNotificationTitle::setText);
        _notificationViewModel.getNotificationContent()
                              .observe(getViewLifecycleOwner(), _binding.tvNotificationContent::setText);
        _notificationViewModel.getBuyerName()
                              .observe(getViewLifecycleOwner(), _binding.tvBuyerName::setText);
        _notificationViewModel.getPriceOffered()
                              .observe(getViewLifecycleOwner(), _binding.tvOfferPrice::setText);

        return _binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _binding = null;
    }


    private void setNotification(String notificationID)
    {
        ProgressBar _loadingProgressBar = _binding.loading;
        _loadingProgressBar.setVisibility(View.VISIBLE);

        DBNotificationManager.getNotification(notificationID).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                _notificationViewModel.setNotification(task.getResult());
                String senderID = task.getResult().getSenderId();

                _binding.ivProfilePicture.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("userID", senderID);
                    Navigation.findNavController(v).navigate(R.id.action_navNotification_to_navProfile, bundle);
                });

                // Fetch buyer details
                DBStorageManager.getProfilePicture(senderID).addOnCompleteListener(sellerTask -> {
                    if (sellerTask.isSuccessful())
                    {
                        DBStorageManager.getProfilePicture(senderID).addOnSuccessListener(
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

            _loadingProgressBar.setVisibility(View.GONE);
        });
    }

}
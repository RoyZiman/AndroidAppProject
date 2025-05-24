package dev.android.project.ui.fragments.inbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import dev.android.project.data.model.notifications.Notification;
import dev.android.project.data.providers.DBNotificationManager;
import dev.android.project.databinding.FragmentInboxBinding;

public class InboxFragment extends Fragment
{
    private FragmentInboxBinding _binding;
    private ProgressBar _loadingProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        _binding = FragmentInboxBinding.inflate(inflater, container, false);

        View root = _binding.getRoot();
        RecyclerView listView = _binding.list;

        _loadingProgressBar = _binding.loading;
        _loadingProgressBar.setVisibility(View.VISIBLE);

        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        Task<ArrayList<Notification>> notificationFetchTask = DBNotificationManager.getAllNotificationsForUser();

        notificationFetchTask.addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listView.setAdapter(new NotificationRecyclerViewAdapter(task.getResult()));
            else
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            _loadingProgressBar.setVisibility(View.GONE);
        });

        return root;
    }
}
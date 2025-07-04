package dev.android.project.ui.fragments.inbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;

import java.util.List;

import dev.android.project.data.models.Notification;
import dev.android.project.data.providers.DBNotificationManager;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.databinding.FragmentItemNotificationBinding;
import dev.android.project.ui.custom.ProfilePictureImageView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification}.
 */
public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder>
{
    private final List<Notification> _values;

    public NotificationRecyclerViewAdapter(List<Notification> items)
    {
        _values = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(FragmentItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),
                                                                      parent,
                                                                      false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        holder.mItem = _values.get(position);
        holder.mNotificationTitle.setText(_values.get(position).getTitle());
        holder.mNotificationContent.setText(_values.get(position).getContent());

        if (holder.mItem.isRead())
        {
            int color = MaterialColors.getColor(holder.itemView,
                                                com.google.android.material.R.attr.colorSurfaceContainer);
            holder.itemView.setBackgroundColor(color);
        }

        DBStorageManager.getProfilePicture(holder.mItem.getSenderId())
                        .addOnSuccessListener(holder.mProfilePictureImageView::setImageBitmap);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            String notificationID = holder.mItem.getId();
            bundle.putString("notificationID", notificationID);
            DBNotificationManager.setNotificationRead(notificationID);
            Navigation.findNavController(v)
                      .navigate(dev.android.project.R.id.action_navInbox_to_navNotification, bundle);
        });
    }

    @Override
    public int getItemCount()
    {
        return _values != null ? _values.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mNotificationTitle;
        public final TextView mNotificationContent;
        public final ProfilePictureImageView mProfilePictureImageView;
        public Notification mItem;

        public ViewHolder(FragmentItemNotificationBinding binding)
        {
            super(binding.getRoot());
            mNotificationTitle = binding.tvNotificationTitle;
            mNotificationContent = binding.tvNotificationContent;
            mProfilePictureImageView = binding.ivProfilePicture;
        }
    }
}
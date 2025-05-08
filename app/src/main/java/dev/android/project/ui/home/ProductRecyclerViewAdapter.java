package dev.android.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.android.project.data.model.Product;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.databinding.FragmentItemProductBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Product}.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>
{

    private final List<Product> mValues;
    private final int mNavigationAction;

    public ProductRecyclerViewAdapter(List<Product> items, int navigationAction)
    {
        mValues = items;
        mNavigationAction = navigationAction;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(FragmentItemProductBinding.inflate(LayoutInflater.from(parent.getContext()),
                                                                 parent,
                                                                 false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDescView.setText(mValues.get(position).getDescription());
        holder.mPriceView.setText(mValues.get(position).getPriceAsString());

        // Call the async getImage function and set the image to an ImageView (assuming you have an ImageView in your
        // layout)
        DBStorageManager.getProductPreview(holder.mItem.getID())
                        .addOnSuccessListener(holder.mImagePreview::setImageBitmap);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("productID", holder.mItem.getID());
            Navigation.findNavController(v).navigate(mNavigationAction, bundle);
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mTitleView;
        public final TextView mDescView;
        public final TextView mPriceView;
        public final ImageView mImagePreview;
        public Product mItem;

        public ViewHolder(FragmentItemProductBinding binding)
        {
            super(binding.getRoot());
            mTitleView = binding.tvProductTitle;
            mDescView = binding.tvProductDescription;
            mPriceView = binding.tvProductPrice;
            mImagePreview = binding.ivProductPreview;
        }
    }


}
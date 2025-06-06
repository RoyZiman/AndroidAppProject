package dev.android.project.ui.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.android.project.data.models.Product;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.databinding.FragmentItemProductBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Product}.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>
{

    private final List<Product> _values;
    private final int _navigationAction;

    public ProductRecyclerViewAdapter(List<Product> items, int navigationAction)
    {
        _values = items;
        _navigationAction = navigationAction;
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
        holder.mItem = _values.get(position);
        holder.mTitleView.setText(_values.get(position).getTitle());
        holder.mDescView.setText(_values.get(position).getDescription());
        holder.mPriceView.setText(_values.get(position).getPriceAsString());

        DBStorageManager.getProductPreview(holder.mItem.getId())
                        .addOnSuccessListener(holder.mImagePreview::setImageBitmap);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("productID", holder.mItem.getId());
            Navigation.findNavController(v).navigate(_navigationAction, bundle);
        });
    }

    @Override
    public int getItemCount()
    {
        return _values.size();
    }

    /**
     * ViewHolder for displaying a Product item in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mTitleView;
        public final TextView mDescView;
        public final TextView mPriceView;
        public final ImageView mImagePreview;
        public Product mItem;

        /**
         * Constructs a new ViewHolder with the provided binding.
         *
         * @param binding The binding for the product item layout.
         */
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
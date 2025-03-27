package dev.android.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.android.project.R;
import dev.android.project.data.model.Product;
import dev.android.project.databinding.FragmentHomeBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Product}.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder>
{

    private final List<Product> mValues;

    public ProductRecyclerViewAdapter(List<Product> items)
    {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(FragmentHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDescView.setText(mValues.get(position).getDescription());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("productID", holder.mItem.getID());
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_productFragment, bundle);
        });
    }

    @Override
    public int getItemCount()
    {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final TextView mTitleView;
        public final TextView mDescView;
        public Product mItem;

        public ViewHolder(FragmentHomeBinding binding)
        {
            super(binding.getRoot());
            mTitleView = binding.tvProductTitle;
            mDescView = binding.tvProductDescription;
        }

        @NonNull
        @Override
        public String toString()
        {
            return super.toString() + " '" + mDescView.getText() + "'";
        }
    }


}
package dev.android.project.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.databinding.FragmentProductListBinding;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment
{
    private static final String ARG_COLUMN_COUNT = "column-count";
    private FragmentProductListBinding _binding;
    private int mColumnCount = 2;

    private ProgressBar _loadingProgressBar;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation
     * changes).
     */
    public HomeFragment()
    {
    }

    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        _binding = FragmentProductListBinding.inflate(inflater, container, false);

        View root = _binding.getRoot();
        View listView = _binding.list;


        _loadingProgressBar = _binding.loading;
        _loadingProgressBar.setVisibility(View.VISIBLE);

        Context context = listView.getContext();
        RecyclerView recyclerView = (RecyclerView)listView;
        if (mColumnCount <= 1)
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

        DBProductsManager.getAllProducts().addOnCompleteListener(task -> {
            if (task.isSuccessful())
                recyclerView.setAdapter(new ProductRecyclerViewAdapter(task.getResult()));
            else
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            _loadingProgressBar.setVisibility(View.GONE);
        });
        return root;
    }
}
package dev.android.project.ui.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import dev.android.project.R;
import dev.android.project.data.model.Product;
import dev.android.project.data.providers.DBProductsManager;
import dev.android.project.databinding.FragmentHomeBinding;

/**
 * A fragment representing a list of Items.
 */
public class HomeFragment extends Fragment
{
    private FragmentHomeBinding _binding;
    private String _filterUserID = null;
    private ProgressBar _loadingProgressBar;

    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            _filterUserID = getArguments().getString("filterUserID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        _binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = _binding.getRoot();
        RecyclerView listView = _binding.list;


        _loadingProgressBar = _binding.loading;
        _loadingProgressBar.setVisibility(View.VISIBLE);

        _binding.fabRemoveFilters.setVisibility(View.GONE);


        if (_filterUserID != null)
        {
            _binding.fabRemoveFilters.setVisibility(View.VISIBLE);
            _binding.fabRemoveFilters.setOnClickListener(v -> {
                _filterUserID = null;
                _binding.fabRemoveFilters.setVisibility(View.GONE);
                Navigation.findNavController(v).popBackStack(R.id.navHome, true);
                Navigation.findNavController(v).navigate(R.id.navHome);
            });
        }

        Context context = listView.getContext();

        listView.setLayoutManager(new GridLayoutManager(context, 2));


        Task<ArrayList<Product>> productFetchTask;
        if (_filterUserID != null)
            productFetchTask = DBProductsManager.getAllProductsByUser(_filterUserID);
        else
            productFetchTask = DBProductsManager.getAllProducts();


        productFetchTask.addOnCompleteListener(task -> {
            if (task.isSuccessful())
                listView.setAdapter(
                        new ProductRecyclerViewAdapter(task.getResult(), R.id.action_navHome_to_navProductView));
            else
                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            _loadingProgressBar.setVisibility(View.GONE);
        });
        return root;
    }
}
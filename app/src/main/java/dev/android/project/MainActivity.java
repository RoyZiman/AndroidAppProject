package dev.android.project;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import dev.android.project.data.model.User;
import dev.android.project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view,
                                                                        "Replace with your own action",
                                                                        Snackbar.LENGTH_LONG)
                                                                  .setAction("Action", null)
                                                                  .setAnchorView(R.id.fab).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navHome, R.id.navCreateListing, R.id.navSettings)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        updateNavigationMenu(navigationView);


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    private void updateNavigationMenu(NavigationView navigationView)
    {
        Menu navMenu = navigationView.getMenu();
        if (User.isLoggedIn())
        {
            navMenu.findItem(R.id.navLogin).setVisible(false);
            navMenu.findItem(R.id.navLogout).setVisible(true)
                   .setOnMenuItemClickListener(item -> {
                       User.signOut();
                       recreate();
                       return true;
                   });
            ;
        }
        else
        {
            navMenu.findItem(R.id.navLogin).setVisible(true);
            navMenu.findItem(R.id.navLogout).setVisible(false);
        }

        if (User.isLoggedIn())
        {
            TextView tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
            TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
            tvUsername.setText(User.getCurrentUser().getName());
            tvEmail.setText(User.getCurrentUser().getEmail());
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
               || super.onSupportNavigateUp();
    }
}


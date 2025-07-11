package dev.android.project;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import dev.android.project.data.models.User;
import dev.android.project.data.providers.DBStorageManager;
import dev.android.project.data.utils.AlarmReceiver;
import dev.android.project.databinding.ActivityMainBinding;
import dev.android.project.ui.custom.ProfilePictureImageView;

public class MainActivity extends AppCompatActivity
{

    private AppBarConfiguration _appBarConfiguration;
    private ActivityMainBinding _binding;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());


        // Request notification permission if necessary (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }

        setSupportActionBar(_binding.appBarMain.toolbar);
        DrawerLayout drawer = _binding.drawerLayout;
        NavigationView navigationView = _binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        _appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navHome, R.id.navCreateListing, R.id.navInbox)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, _appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateNavigationMenu(_binding.navView);
    }

    private void updateNavigationMenu(NavigationView navigationView)
    {

        Menu navMenu = navigationView.getMenu();
        if (User.isLoggedIn())
        {
            navMenu.findItem(R.id.navProfile).setVisible(true);
            navMenu.findItem(R.id.navLogin).setVisible(false);
            navMenu.findItem(R.id.navLogout).setVisible(true)
                   .setOnMenuItemClickListener(item -> {
                       User.signOut();
                       recreate();
                       return true;
                   });
        }
        else
        {
            navMenu.findItem(R.id.navProfile).setVisible(false);
            navMenu.findItem(R.id.navLogin).setVisible(true);
            navMenu.findItem(R.id.navLogout).setVisible(false);
        }

        if (User.isLoggedIn())
        {
            TextView tvUsername = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
            TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);
            ProfilePictureImageView ivUserImage = navigationView.getHeaderView(0).findViewById(R.id.ivProfilePicture);
            tvUsername.setText(User.getCurrentUser().getName());
            tvEmail.setText(User.getCurrentUser().getEmail());

            DBStorageManager.getProfilePicture(User.getCurrentUser().getId())
                            .addOnSuccessListener(bitmap -> ivUserImage.setImageBitmap(bitmap));

            ivUserImage.setOnClickListener(v -> {
                _binding.drawerLayout.closeDrawer(_binding.navView);
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.navProfile);
            });
        }
    }

    @Override
    protected void onDestroy()
    {
        AlarmReceiver.scheduleNotification(this, 300); // Schedule a notification after 5 minutes
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, _appBarConfiguration)
               || super.onSupportNavigateUp();
    }
}


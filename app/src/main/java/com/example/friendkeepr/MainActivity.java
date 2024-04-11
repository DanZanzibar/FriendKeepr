//******************************************************************************
//  MainActivity.java
//
//  Zan Owsley T00745703
//  COMP 2161
//  This is the single 'Activity' class for the app. It houses the
//  'NavHostFragment' that displays all other Fragments. It sets up the
//  'NavController', 'ToolBar' (for a top app bar), 'BottomNavigationView', and
//  stores the only instance of 'FriendKeeprData' in the app. It also requests
//  permissions upon starting the app and will display a screen indicating that
//  permissions are needed if not given.
//******************************************************************************
package com.example.friendkeepr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FriendKeeprData appData;
    private NavController navController;
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    //--------------------------------------------------------------------------
    //  This method is responsible for setting up the app as the only point of
    //  entry. If creates a 'ActivityResultLauncher' that will launch the app
    //  if permissions come back granted and handles requesting the permissions.
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),

		// This is the method invoked when the boolean Map 'isGranted'
		// is returned. If all permissions are granted, launches the app.
                isGranted -> {
                    boolean allPermissionsGranted = true;
                    for (Boolean granted : isGranted.values()) {
                        if (!granted) {
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                    if (allPermissionsGranted) {
                        launchApp();
                    }
                }
        );

	// If permission are already granted, start the app. Otherwise, show
	// the screen indicating permission are needed.
        if (areCorePermissionsGranted()) {
            launchApp();
        }
        else {
            setContentView(R.layout.permissions_needed);
            getCorePermissions();
        }
    }

    //--------------------------------------------------------------------------
    //  This method is responsible for directing the app to the correct
    //  'Fragment' when items in the app bar are selected.
    //--------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------
    //  This helper method continues app started once permissions are granted.
    //--------------------------------------------------------------------------
    private void launchApp() {
        setContentView(R.layout.activity_main);

	// Set up the Navigation Component.
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(navHostFragment);

	// Set up the top app bar.
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(
                        R.id.alerts_fragment,
                        R.id.friends_fragment,
                        R.id.groups_fragment,
                        R.id.import_fragment
                ).build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(
                item -> {
                    if (item.getItemId() == R.id.settings_fragment) {
                        navController.navigate(R.id.settings_fragment);
                        return true;
                    } else
                        return false;
                }
        );

	// Set up bottom navigation.
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(
                toolbar,
                navController,
                appBarConfiguration
        );
        NavigationUI.setupWithNavController(bottomNav, navController);

	// Create the single instance of 'FriendKeeprData'.
        appData = new FriendKeeprData(this);
    }

    //--------------------------------------------------------------------------
    //  This method returns the single instance of 'FriendKeeprData'. It will
    //  be used by all Fragments in the app to access it.
    //--------------------------------------------------------------------------
    public FriendKeeprData getAppData() {
        return appData;
    }

    //--------------------------------------------------------------------------
    //  This helper method checks if core permissions are granted.
    //--------------------------------------------------------------------------
    private boolean areCorePermissionsGranted() {
        return !(needsPermission(Manifest.permission.READ_CONTACTS) ||
                needsPermission(Manifest.permission.READ_CALL_LOG) ||
                needsPermission(Manifest.permission.READ_SMS)) ||
                needsPermission(Manifest.permission.CALL_PHONE) ||
                needsPermission(Manifest.permission.SEND_SMS);
    }

    //--------------------------------------------------------------------------
    //  This method launches a request for any permissions needed that are not
    //  granted.
    //--------------------------------------------------------------------------
    private void getCorePermissions() {
        List<String> neededPermissionsList = new ArrayList<>();

	// Check for permissions one-by-one and add to list if needed.
        if (needsPermission(Manifest.permission.READ_CONTACTS))
            neededPermissionsList.add(Manifest.permission.READ_CONTACTS);
        if (needsPermission(Manifest.permission.READ_CALL_LOG))
            neededPermissionsList.add(Manifest.permission.READ_CALL_LOG);
        if (needsPermission(Manifest.permission.READ_SMS))
            neededPermissionsList.add(Manifest.permission.READ_SMS);
        if (needsPermission(Manifest.permission.CALL_PHONE))
            neededPermissionsList.add(Manifest.permission.CALL_PHONE);
        if (needsPermission(Manifest.permission.SEND_SMS))
            neededPermissionsList.add(Manifest.permission.SEND_SMS);

        String[] neededPermissions = neededPermissionsList.toArray(new String[0]);

        requestPermissionLauncher.launch(neededPermissions);
    }

    //--------------------------------------------------------------------------
    //  This method checks if a given permission is granted or not.
    //--------------------------------------------------------------------------
    private boolean needsPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED;
    }
}

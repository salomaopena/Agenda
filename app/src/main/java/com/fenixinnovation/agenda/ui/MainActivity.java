package com.fenixinnovation.agenda.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fenixinnovation.agenda.R;
import com.fenixinnovation.agenda.fragments.AboutFragment;
import com.fenixinnovation.agenda.fragments.CalendarFragment;
import com.fenixinnovation.agenda.fragments.CallsFragment;
import com.fenixinnovation.agenda.fragments.EmailsFragment;
import com.fenixinnovation.agenda.fragments.HomeFragment;
import com.fenixinnovation.agenda.fragments.MessagesFragment;
import com.fenixinnovation.agenda.fragments.NewContactFragment;
import com.fenixinnovation.agenda.fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFragment(new HomeFragment());

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                loadFragment(new HomeFragment());
                break;
            case R.id.nav_new_contact:
                loadFragment(new NewContactFragment());
                break;
            case R.id.nav_calls:
                loadFragment(new CallsFragment());
                break;
            case R.id.nav_messages:
                loadFragment(new MessagesFragment());
                break;
            case R.id.nav_emails:
                loadFragment(new EmailsFragment());
                break;
            case R.id.nav_calendar:
               loadFragment(new CalendarFragment());
                break;
            case R.id.nav_about:
                loadFragment(new AboutFragment());
                break;
            case R.id.nav_settings:
                loadFragment(new SettingsFragment());
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.inc, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setCustomAnimations(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}

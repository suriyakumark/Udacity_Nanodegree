package com.simplesolutions2003.happybabycare;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    public static boolean USER_LOGGED_IN = false;
    public static DrawerLayout drawer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!USER_LOGGED_IN){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Fragment fragment = new SignInFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }else{
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        }

    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "popping backstack");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i(LOG_TAG, "popping backstack");
                fm.popBackStack();
            } else {
                Log.i(LOG_TAG, "nothing on backstack, calling super");
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_select_baby);
        Spinner spinnerSelectBaby = (Spinner) MenuItemCompat.getActionView(item);
        final String[] arraySelectBaby = new String[] {" + Add Baby"};
        ArrayAdapter<String> adapterSelectBaby = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySelectBaby);
        spinnerSelectBaby.setAdapter(adapterSelectBaby);
        spinnerSelectBaby.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getBaseContext(), arraySelectBaby[position], Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_baby_profile) {
            return true;
        }
        if (id == R.id.action_manage_group) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean addFragmentToStack = false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Fragment fragment = null;
        switch(id) {
            case R.id.nav_activities:
                fragment = new ActivitiesFragment();
                addFragmentToStack = true;
                break;
            case R.id.nav_feeding:
                fragment = new FeedingFragment();
                break;
            case R.id.nav_diaper:
                fragment = new DiaperFragment();
                break;
            case R.id.nav_sleeping:
                fragment = new SleepingFragment();
                break;
            case R.id.nav_health:
                fragment = new HealthFragment();
                break;
            case R.id.nav_stories:
                fragment = new StoriesFragment();
                addFragmentToStack = true;
                break;
            case R.id.nav_rhymes:
                fragment = new RhymesFragment();
                addFragmentToStack = true;
                break;
            case R.id.nav_sounds:
                fragment = new SoundsFragment();
                break;
            case R.id.nav_settings:
                //setContentView(R.layout.settings);
                break;
            case R.id.nav_signout:
                SignInFragment.ACTION_SIGN_OUT = true;
                fragment = new SignInFragment();
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            if(addFragmentToStack) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment)
                        .commit();
            }
            //setTitle(navMenuTitles[position]);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SignInFragment.RC_SIGN_IN) {
            SignInFragment fragment = (SignInFragment) getFragmentManager()
                    .findFragmentById(R.id.sign_in_layout);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}

package com.simplesolutions2003.happybabycare;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.simplesolutions2003.happybabycare.sync.ArticleSyncAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    public static boolean USER_LOGGED_IN = false;
    public static long LOGGED_IN_USER_ID_ID = -1;
    public static String LOGGED_IN_USER_ID = null;
    public static String GROUP_ID = null;
    public static long ACTIVE_BABY_ID = -1;
    public static String ACTIVE_BABY_NAME = null;
    public static boolean FRAGMENT_REQUIRES_BABY_ID = false;
    public static DrawerLayout drawer = null;
    NavigationView navigationView;
    public static MenuItem babyProfilesMenuItem;
    public static MenuItem manageGroupMenuItem;

    MenuItem itemPrev;
    Toolbar toolbar;
    FragmentManager fragmentManager = getFragmentManager();
    static String prevFragmentTag = null;
    static String currFragmentTag = null;
    static boolean keepFragmentInStack = false;
    static boolean keepPrevFragmentInStack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.ad_device_id))
                .build();
        mAdView.loadAd(adRequest);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!USER_LOGGED_IN){
            handleFragments(new SignInFragment(),SignInFragment.TAG,SignInFragment.KEEP_IN_STACK);
        }

        Log.v(TAG, "initializeSyncAdapter");
        ArticleSyncAdapter.initializeSyncAdapter(this);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(fragmentManager.getBackStackEntryCount() == 0 & fragmentManager.findFragmentByTag(SignInFragment.TAG) == null){
            handleFragments(new SignInFragment(),SignInFragment.TAG,SignInFragment.KEEP_IN_STACK);
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Log.i(TAG, "closeDrawer");
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                Log.i(TAG, "popping backstack " + fragmentManager.getBackStackEntryCount());
                //fragmentManager.popBackStackImmediate(prevFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //Log.v(TAG, "popping backstack > " + fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1));
                //currFragmentTag = prevFragmentTag;
                //keepFragmentInStack = keepPrevFragmentInStack;
                keepPrevFragmentInStack = true;
                fragmentManager.popBackStack();
            } else {
                Log.i(TAG, "nothing on backstack, calling super");
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        babyProfilesMenuItem = (MenuItem)  menu.findItem(R.id.action_baby_profiles);
        manageGroupMenuItem = (MenuItem)  menu.findItem(R.id.action_manage_group);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        updateMenuVisibility(this);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_baby_profiles) {
            handleFragments(new BabyFragment(),BabyFragment.TAG,BabyFragment.KEEP_IN_STACK);
        }
        if (id == R.id.action_manage_group) {
            if(GROUP_ID != null) {
                handleFragments(new GroupManageFragment(), GroupManageFragment.TAG, GroupManageFragment.KEEP_IN_STACK);
            }else{
                handleFragments(new GroupFragment(), GroupFragment.TAG, GroupFragment.KEEP_IN_STACK);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if (itemPrev != null) {
            itemPrev.setChecked(false);
        }
        item.setChecked(true);
        itemPrev = item;

        final int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (id) {
                    case R.id.nav_activities:
                        handleFragments(new ActivitiesFragment(), ActivitiesFragment.TAG, ActivitiesFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_feeding:
                        handleFragments(new FeedingFragment(), FeedingFragment.TAG, FeedingFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_diaper:
                        handleFragments(new DiaperFragment(), DiaperFragment.TAG, DiaperFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_sleeping:
                        handleFragments(new SleepingFragment(), SleepingFragment.TAG, SleepingFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_health:
                        handleFragments(new HealthFragment(), HealthFragment.TAG, HealthFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_stories:
                        ArticlesFragment.ARTICLE_TYPE = "stories";
                        handleFragments(new ArticlesFragment(), ArticlesFragment.TAG, ArticlesFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_rhymes:
                        ArticlesFragment.ARTICLE_TYPE = "rhymes";
                        handleFragments(new ArticlesFragment(), ArticlesFragment.TAG, ArticlesFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_sounds:
                        handleFragments(new SoundsFragment(), SoundsFragment.TAG, SoundsFragment.KEEP_IN_STACK);
                        break;
                    case R.id.nav_settings:
                        //setContentView(R.layout.settings);
                        break;
                    case R.id.nav_signout:
                        SignInFragment.ACTION_SIGN_OUT = true;
                        handleFragments(new SignInFragment(), SignInFragment.TAG, SignInFragment.KEEP_IN_STACK);
                        break;
                }
            }
        },300);
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

    public static void updateMenuVisibility(Activity activity){
        if(USER_LOGGED_IN) {
            babyProfilesMenuItem.setVisible(true);
            manageGroupMenuItem.setVisible(true);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else{
            babyProfilesMenuItem.setVisible(false);
            manageGroupMenuItem.setVisible(false);
            drawer.closeDrawer(GravityCompat.START);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void handleFragments(Fragment fragment, String tag, boolean addToStack){

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        if(MainActivity.ACTIVE_BABY_ID == -1 & (tag.equals(ActivitiesFragment.TAG) |
                tag.equals(FeedingFragment.TAG) |
                tag.equals(DiaperFragment.TAG) |
                tag.equals(SleepingFragment.TAG) |
                tag.equals(HealthFragment.TAG))){
            fragment = null;
            if (itemPrev != null) {
                itemPrev.setChecked(false);
            }
            Toast.makeText(this, "Please select a baby", Toast.LENGTH_LONG).show();
        }

        if (fragment != null) {
            currFragmentTag = tag;
            keepFragmentInStack = addToStack;
            Log.v(TAG, "handleFragments - " + currFragmentTag + ";" + keepFragmentInStack + ";" + prevFragmentTag + ";" + keepPrevFragmentInStack);

            if(currFragmentTag.equals(SignInFragment.TAG) | keepFragmentInStack){
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            if(keepPrevFragmentInStack){
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment,currFragmentTag)
                        .addToBackStack(currFragmentTag)
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, fragment,currFragmentTag)
                        .commit();
            }
            prevFragmentTag = currFragmentTag;
            keepPrevFragmentInStack = keepFragmentInStack;

            //setTitle(navMenuTitles[position]);

        }
        Log.v(TAG,"handleFragments - " + fragmentManager.getBackStackEntryCount());
    }

    public void handleFragments(boolean goBack){
        if(goBack) {
            onBackPressed();
        }
    }

    public void updateToolbarTitle(String title){
        if(toolbar != null) {
            if (title == null) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            } else {
                getSupportActionBar().setTitle(title);
            }
        }
    }
}


package com.lucifer.auto_h.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lucifer.auto_h.LoginActivity;
import com.lucifer.auto_h.R;
import com.lucifer.auto_h.admin.AdminPanel;
import com.lucifer.auto_h.bell.BellService;

import static com.lucifer.auto_h.Constants.ADMIN;
import static com.lucifer.auto_h.Constants.GUEST;
import static com.lucifer.auto_h.Constants.LOGGED_OUT;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    int[] arr = new int[]{R.id.navigation_controls, R.id.navigation_status, R.id.navigation_report, R.id.navigation_settings};
    SharedPreferences preferences;
    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(getApplicationContext(), BellService.class));
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getInt(getString(R.string.logged_in_as), GUEST) == LOGGED_OUT) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }/* else if (preferences.getInt(getString(R.string.logged_in_as), GUEST) == GUEST) {
            startActivity(new Intent(MainActivity.this, GuestActivity.class));
            finish();
        }*/
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    navigation.setSelectedItemId(arr[mViewPager.getCurrentItem()]);
//    navigation.setSe
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getInt(getString(R.string.logged_in_as), ADMIN) == ADMIN) {
            getMenuInflater().inflate(R.menu.menu_main_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_controls:
                mViewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_status:
                mViewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_report:
                mViewPager.setCurrentItem(2);
                return true;
            case R.id.navigation_settings:
                mViewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(getString(R.string.logged_in_as), LOGGED_OUT);
            editor.apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        if (id == R.id.action_admin) {
            startActivity(new Intent(MainActivity.this, AdminPanel.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.5f;
        private static final float MIN_ALPHA = 0.4f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1)
                view.setAlpha(0);
            else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0)
                    view.setTranslationX(horzMargin - vertMargin / 2);
                else
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else view.setAlpha(0);

        }
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new ControlFragment();
                case 1:
                    return new StatusFragment();
                case 2:
                    return new ReportFragment();
                case 3:
                    return new PreferenceFragment();
            }
            return new Fragment();

        }

        @Override
        public int getCount() {

            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CONTROL";
                case 1:
                    return "STATUS";
                case 2:
                    return "REPORT";
                case 3:
                    return "PREFERENCES";
            }
            return "NULL";
        }
    }

}

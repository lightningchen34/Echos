package com.chen91apps.echos;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.chen91apps.echos.fragments.ListFragment;
import com.chen91apps.echos.fragments.PageFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PageFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener {

    private TextView searchTextView;
    private ArrayList<PageFragment> pages;
    private ArrayList<Pair<String, String>> tabText;

    private PageFragment currentFragment = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        // config.locale = Locale.CHINA;
        resources.updateConfiguration(config, dm);

        super.onCreate(savedInstanceState);
        // this.setTheme(R.style.Mytheme_Night);
        setContentView(R.layout.activity_main);

        tabText = new ArrayList<Pair<String, String>>();
        tabText.add(new Pair<String, String>(getString(R.string.mainactivaty_text_news), getString(R.string.mainactivaty_tag_news)));
        tabText.add(new Pair<String, String>(getString(R.string.mainactivaty_text_community), getString(R.string.mainactivaty_tag_community)));
        tabText.add(new Pair<String, String>(getString(R.string.mainactivaty_text_rss), getString(R.string.mainactivaty_tag_rss)));

        initTabs();
        initPages();

        initNavigationView();
        initSearchText();
    }

    private void initNavigationView()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initSearchText()
    {
        searchTextView = (TextView) findViewById(R.id.search_textview);
        searchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == searchTextView.getId())
                {
                    Toast.makeText(view.getContext(), "Go to SearchActivity", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initPages()
    {
        System.out.println("Init Fragment");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        pages = new ArrayList<PageFragment>();
        for (Pair<String, String> text : tabText)
        {
            pages.add(PageFragment.newInstance(text.second, ""));
        }
        showFragment(0);
    }

    private void initTabs()
    {
        TabLayout tabLayout = findViewById(R.id.main_tablayout);
        for (Pair<String, String> text : tabText)
        {
            tabLayout.addTab(tabLayout.newTab().setText(text.first).setTag(text.second));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println(tab.getPosition());
                int index = tab.getPosition();
                showFragment(index);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("Unselect Tab");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("Reselect Tab");

            }
        });
    }

    private void showFragment(int index)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (currentFragment != null)
        {
            fragmentTransaction.hide(currentFragment);
        }
        currentFragment = pages.get(index);
        if (currentFragment.isAdded()) {
            fragmentTransaction.show(currentFragment);
        } else
        {
            fragmentTransaction.add(R.id.main_framelayout, currentFragment, currentFragment.getTag());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageFragmentInteraction(Uri uri) {
        // TODO
    }

    @Override
    public void onListFragmentInteraction(Uri uri) {
        // TODO
    }
}

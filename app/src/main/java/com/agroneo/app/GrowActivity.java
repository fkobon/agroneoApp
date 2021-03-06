package com.agroneo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.agroneo.app.discuss.DiscussFragment;
import com.agroneo.app.gaia.GaiaFragment;
import com.agroneo.app.pages.PagesFragment;
import com.agroneo.app.profile.ProfileFragment;
import com.agroneo.app.ui.ActionBarCtl;

import java.util.ArrayList;
import java.util.List;

public class GrowActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final List<String> history = new ArrayList<>();
    private GaiaFragment gaia;
    private DiscussFragment discuss;
    private ProfileFragment profile;
    private PagesFragment documents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  AppDatabase.getAppDatabase(this).clearAllTables();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarCtl actionbarCtl = new ActionBarCtl(getSupportActionBar());

        gaia = new GaiaFragment().setActionbar(actionbarCtl);
        discuss = new DiscussFragment().setActionbar(actionbarCtl);
        profile = new ProfileFragment().setActionbar(actionbarCtl);
        documents = new PagesFragment().setActionbar(actionbarCtl);

        setFragment(discuss);

        //TODO: mauvaise gestion de l'history, ne pas logger les fragments, préférer un modèle basé sur l'URL qui activerai le fragment.
        history.add("agroneo:///forum");

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        if (intent.getData() != null) {
            String sheme = intent.getData().getScheme();
            if (sheme.startsWith("http")) {
                getBaseContext().startActivity(intent);

            } else if (sheme.equals("agroneo")) {

                String path = intent.getData().getPath();
                if (path.startsWith("/forum")) {

                    discuss.load(path);
                    history.add(intent.getData().toString());
                    return;
                } else {
                    documents.loadPage(path);
                    history.add(intent.getData().toString());
                    return;
                }
            }
        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            history.remove(history.size() - 1);
            if (history.size() == 0) {
                super.onBackPressed();
                return;
            }
            String last = history.get(history.size() - 1);
            if (last.startsWith("agroneo")) {
                Uri urilast = Uri.parse(last);
                if (urilast.getScheme().equals("agroneo")) {
                    String path = urilast.getPath();
                    if (path.startsWith("/forum")) {
                        discuss.load(path);
                        return;
                    } else {
                        documents.loadPage(urilast.getPath());
                        return;
                    }
                }
            }
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            List<Fragment> frags = manager.getFragments();
            for (Fragment frag : frags) {
                if (frag.getClass().getName() == last) {
                    transaction.show(frag);
                } else {
                    transaction.hide(frag);
                }
            }
            transaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                setFragment(gaia);
                break;
            case R.id.nav_discuss:
                setFragment(discuss);
                break;
            case R.id.nav_profile:
                setFragment(profile);
                break;
            case R.id.nav_documents:
                setFragment(documents);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setFragment(Fragment fragment) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment frag : manager.getFragments()) {
            if (frag != fragment) {
                transaction.hide(frag);
            }
        }
        if (!fragment.isAdded()) {
            transaction.add(R.id.frameContainer, fragment);
            history.add(fragment.getClass().getName());
        } else if (fragment.isAdded() && fragment.isHidden()) {
            transaction.show(fragment);
            history.add(fragment.getClass().getName());
        }
        transaction.commit();
    }

}

package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;
import costy.krzysiekz.com.costy.view.activity.fragment.ExpensesFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.ReportFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.SettingsFragment;

public class SelectedProjectActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String PROJECT_NAME = "PROJECT_NAME";
    private static final String STATE_FRAGMENT = "STATE_FRAGMENT";

    @BindView(R.id.selected_project_nav_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.selected_project_toolbar)
    Toolbar toolbar;

    @BindView(R.id.selected_project_nav_view)
    NavigationView navigationView;

    private String projectName;
    private int currentFragment;

    SelectedProjectPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_project);

        CostyApplication.component().inject(this);
        ButterKnife.bind(this);

        setUpNavigationDrawer();

        projectName = getIntent().getStringExtra(PROJECT_NAME);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_FRAGMENT)) {
            MenuItem item = navigationView.getMenu().findItem(savedInstanceState.getInt(STATE_FRAGMENT));
            onNavigationItemSelected(item);
        } else {
            setInitialFragment();
        }
    }

    private void setUpNavigationDrawer() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            TextView projectNameTextView = (TextView) v.findViewById(R.id.nav_header_project_name);
            if (projectNameTextView != null) {
                projectNameTextView.setText(projectName);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setInitialFragment() {
        if (presenter.checkIfPeopleAdded(projectName)) {
            showFragment(new ExpensesFragment());
            setTitle(R.string.nav_drawer_option_expenses);
            currentFragment = R.id.nav_expenses;
        } else {
            showFragment(new PeopleFragment());
            setTitle(R.string.nav_drawer_option_people);
            currentFragment = R.id.nav_people;
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_expenses) {
            fragment = new ExpensesFragment();
        } else if (id == R.id.nav_people) {
            fragment = new PeopleFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_report) {
            fragment = new ReportFragment();
        } else if (id == R.id.nav_change_project) {

        }

        if (fragment != null) {
            showFragment(fragment);
            item.setChecked(true);
            setTitle(item.getTitle());
            drawerLayout.closeDrawers();
            currentFragment = id;
            return true;
        } else {
            return false;
        }

    }

    private void showFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString(PROJECT_NAME, projectName);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.selected_project_content, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_FRAGMENT, currentFragment);
        super.onSaveInstanceState(outState);
    }

    @Inject
    void setPresenter(SelectedProjectPresenter presenter) {
        this.presenter = presenter;
    }
}

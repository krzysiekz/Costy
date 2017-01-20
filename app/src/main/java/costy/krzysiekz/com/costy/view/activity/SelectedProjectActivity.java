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
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;
import costy.krzysiekz.com.costy.view.activity.fragment.ExpensesFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.SettingsFragment;

public class SelectedProjectActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String PROJECT_NAME = "PROJECT_NAME";

    @BindView(R.id.selected_project_nav_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String projectName;

    SelectedProjectPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_project);

        CostyApplication.component().inject(this);
        ButterKnife.bind(this);

        setUpNavigationDrawer();

        projectName = getIntent().getStringExtra(PROJECT_NAME);

        setInitialFragment();
    }

    private void setUpNavigationDrawer() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.selected_project_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setInitialFragment() {
        if (presenter.checkIfPeopleAdded(projectName)) {
            showFragment(new ExpensesFragment());
            setTitle(R.string.nav_drawer_option_expenses);
        } else {
            showFragment(new PeopleFragment());
            setTitle(R.string.nav_drawer_option_people);
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
        } else if (id == R.id.nav_change_project) {

        }

        if (fragment != null) {
            showFragment(fragment);
            item.setChecked(true);
            setTitle(item.getTitle());
            drawerLayout.closeDrawers();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selected_project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Inject
    void setPresenter(SelectedProjectPresenter presenter) {
        this.presenter = presenter;
    }
}

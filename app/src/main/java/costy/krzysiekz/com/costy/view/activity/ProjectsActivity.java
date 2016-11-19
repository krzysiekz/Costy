package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;

public class ProjectsActivity extends AppCompatActivity implements ProjectsView {

    private ProjectsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        CostyApplication.component().inject(this);
        presenter.attachView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Inject
    void setPresenter(ProjectsPresenter presenter) {
        this.presenter = presenter;
    }

    ProjectsPresenter getPresenter() {
        return presenter;
    }
}

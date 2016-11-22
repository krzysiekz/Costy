package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogListener;

public class ProjectsActivity extends AppCompatActivity implements ProjectsView,
        AddProjectDialogListener {

    ProjectsPresenter presenter;

    @BindView(R.id.add_project_button)
    FloatingActionButton addProjectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        CostyApplication.component().inject(this);
        presenter.attachView(this);

        ButterKnife.bind(this);

        addProjectButton.setOnClickListener(__ -> showAddProjectDialog());
    }

    private void showAddProjectDialog() {
        AddProjectDialogFragment dialogFragment = new AddProjectDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), AddProjectDialogFragment.TAG);
    }

    @Inject
    void setPresenter(ProjectsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onProjectNameConfirmed(String projectName) {

    }
}

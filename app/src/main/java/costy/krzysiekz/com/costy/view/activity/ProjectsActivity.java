package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;
import costy.krzysiekz.com.costy.view.activity.adapter.ProjectAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogListener;

public class ProjectsActivity extends AppCompatActivity implements ProjectsView,
        AddProjectDialogListener {

    ProjectsPresenter presenter;

    @BindView(R.id.add_project_button)
    FloatingActionButton addProjectButton;

    @BindView(R.id.projects_recycler_view)
    RecyclerView projectsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        CostyApplication.component().inject(this);
        ButterKnife.bind(this);

        presenter.attachView(this);
        setupProjectsRecycleView();

        addProjectButton.setOnClickListener(__ -> showAddProjectDialog());
    }

    private void showAddProjectDialog() {
        AddProjectDialogFragment dialogFragment = new AddProjectDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), AddProjectDialogFragment.TAG);
    }

    private void setupProjectsRecycleView() {
        ProjectAdapter adapter = new ProjectAdapter();
        projectsRecyclerView.setAdapter(adapter);
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Inject
    void setPresenter(ProjectsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onProjectNameConfirmed(String projectName) {
        presenter.addProject(projectName);
    }

    @Override
    public void showProjects(List<ExpenseProject> expenseProjects) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.setProjects(expenseProjects);
        adapter.notifyDataSetChanged();
    }
}

package costy.krzysiekz.com.costy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;
import costy.krzysiekz.com.costy.view.activity.adapter.ClickListener;
import costy.krzysiekz.com.costy.view.activity.adapter.ProjectAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogListener;

public class ProjectsActivity extends AppCompatActivity implements ProjectsView,
        AddProjectDialogListener, ClickListener, ActionMode.Callback {

    ActionMode actionMode;
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
        ProjectAdapter adapter = new ProjectAdapter(this);
        projectsRecyclerView.setAdapter(adapter);
        projectsRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
    public void showWrongNameError() {
        Toast.makeText(this.getApplicationContext(), R.string.wrong_project_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProjects(List<ExpenseProject> expenseProjects) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.setProjects(expenseProjects);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeProjects(Set<Integer> positions) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.removeItems(new ArrayList<>(positions));
        actionMode.finish();
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
            Intent intent = new Intent(this, SelectedProjectActivity.class);
            intent.putExtra(SelectedProjectActivity.PROJECT_NAME,
                    adapter.getItems().get(position).getName());
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(this);
        }

        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        switch (item.getItemId()) {
            case R.id.menu_remove:
                presenter.removeProjects(adapter.getSelectedProjects());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.clearSelection();
        actionMode = null;
    }
}

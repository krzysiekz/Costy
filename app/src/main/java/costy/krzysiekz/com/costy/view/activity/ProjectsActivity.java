package costy.krzysiekz.com.costy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;
import costy.krzysiekz.com.costy.view.SelectableView;
import costy.krzysiekz.com.costy.view.activity.adapter.ProjectAdapter;
import costy.krzysiekz.com.costy.view.activity.adapter.SelectableAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogListener;

public class ProjectsActivity extends AppCompatActivity implements ProjectsView,
        AddProjectDialogListener, SelectableView<ExpenseProject> {

    ActionMode actionMode;
    ProjectsPresenter presenter;

    @BindView(R.id.add_project_button)
    FloatingActionButton addProjectButton;

    @BindView(R.id.projects_recycler_view)
    RecyclerView projectsRecyclerView;

    SelectableViewHandler<ExpenseProject> selectableViewHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        CostyApplication.component().inject(this);
        ButterKnife.bind(this);

        presenter.attachView(this);
        setupProjectsRecycleView();

        selectableViewHandler = new SelectableViewHandler<>(this);

        addProjectButton.setOnClickListener(__ -> presenter.showAddProjectDialog());
        presenter.loadProjects();
    }

    @Override
    public void showAddProjectDialog(List<Currency> currencies) {
        AddProjectDialogFragment dialogFragment = new AddProjectDialogFragment();
        dialogFragment.setCurrencies(currencies);
        dialogFragment.show(getSupportFragmentManager(), AddProjectDialogFragment.TAG);
    }

    private void setupProjectsRecycleView() {
        ProjectAdapter adapter = new ProjectAdapter(selectableViewHandler);
        projectsRecyclerView.setAdapter(adapter);
        projectsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Inject
    void setPresenter(ProjectsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onProjectConfirmed(ExpenseProject project) {
        presenter.addProject(project);
    }

    @Override
    public void showWrongNameError() {
        Toast.makeText(this.getApplicationContext(), R.string.wrong_project_name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProjects(List<ExpenseProject> expenseProjects) {
        ProjectAdapter adapter = (ProjectAdapter) projectsRecyclerView.getAdapter();
        adapter.setItems(expenseProjects);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeProjects(Set<Integer> positions) {
        selectableViewHandler.removeItems(positions);
    }

    @Override
    public SelectableAdapter<?, ExpenseProject> getAdapter() {
        return (ProjectAdapter) projectsRecyclerView.getAdapter();
    }

    @Override
    public void handleRemoveButtonClicked(Map<Integer, ExpenseProject> selectedItemObjects) {
        presenter.removeProjects(selectedItemObjects);
    }

    @Override
    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    @Override
    public ActionMode getActionMode() {
        return actionMode;
    }

    @Override
    public void handleSingleItemClick(ExpenseProject clickedItem) {
        Intent intent = new Intent(this, SelectedProjectActivity.class);
        intent.putExtra(SelectedProjectActivity.PROJECT_NAME, clickedItem.getName());
        startActivity(intent);
    }

    @Override
    public ActionMode startSupportActionMode() {
        return startSupportActionMode(selectableViewHandler);
    }
}

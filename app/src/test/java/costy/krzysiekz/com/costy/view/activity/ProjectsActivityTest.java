package costy.krzysiekz.com.costy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.view.ProjectsView;
import costy.krzysiekz.com.costy.view.activity.adapter.ProjectAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddProjectDialogFragment;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24,
        application = TestCostyApplication.class)
public class ProjectsActivityTest {

    private ProjectsActivity projectsActivity;
    private PresenterModuleMock presenterModuleMock;

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        projectsActivity = Robolectric.buildActivity(ProjectsActivity.class).
                create().start().resume().get();
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldExist() {
        //then
        assertThat(projectsActivity).isNotNull();
    }

    @Test
    public void shouldImplementProjectsView() {
        //then
        assertThat(projectsActivity).isInstanceOf(ProjectsView.class);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(projectsActivity.presenter).isNotNull();
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getProjectsPresenter()).attachView(projectsActivity);
    }

    @Test
    public void shouldBindAddProjectButton() {
        //then
        assertThat(projectsActivity.addProjectButton).isNotNull();
    }

    @Test
    public void shouldCallDialogFragmentAfterClickingAddProjectButton() {
        //when
        projectsActivity.addProjectButton.performClick();
        Fragment fragment = projectsActivity.getSupportFragmentManager().
                findFragmentByTag(AddProjectDialogFragment.TAG);
        //then
        assertThat(fragment).isNotNull();
        assertThat(fragment).isInstanceOf(AddProjectDialogFragment.class);
    }

    @Test
    public void shouldCallPresenterWhenProjectNameConfirmed() {
        //given
        String projectName = "Some project";
        Currency defaultCurrency = new Currency("EUR");
        //when
        projectsActivity.onProjectNameConfirmed(projectName, defaultCurrency);
        //then
        verify(presenterModuleMock.getProjectsPresenter()).addProject(projectName, defaultCurrency);
    }

    @Test
    public void shouldShowProjects() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject project1 = new ExpenseProject("Project 1", defaultCurrency);
        ExpenseProject project2 = new ExpenseProject("Project 2", defaultCurrency);
        //when
        RecyclerView recyclerView = projectsActivity.projectsRecyclerView;
        projectsActivity.showProjects(Arrays.asList(project1, project2));
        //then
        assertThat(recyclerView).isNotNull();
        assertThat(recyclerView.getAdapter()).isInstanceOf(ProjectAdapter.class);
        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(2);
        assertThat(((ProjectAdapter) recyclerView.getAdapter()).getItems()).containsOnly(project1, project2);
    }

    @Test
    public void shouldStartExpensesActivityAfterClickingOnProject() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject project1 = new ExpenseProject("Project 1", defaultCurrency);
        //when
        projectsActivity.showProjects(Collections.singletonList(project1));
        projectsActivity.projectsRecyclerView.measure(0, 0);
        projectsActivity.projectsRecyclerView.layout(0, 0, 100, 10000);
        projectsActivity.projectsRecyclerView.findViewHolderForAdapterPosition(0).
                itemView.performClick();
        Intent intent = shadowOf(projectsActivity).peekNextStartedActivity();
        //then
        assertThat(intent.getComponent().getClassName()).isEqualTo(SelectedProjectActivity.class.getName());
        assertThat(intent.getStringExtra(SelectedProjectActivity.PROJECT_NAME)).isEqualTo(project1.getName());
    }

    @Test
    public void shouldShowToastWhenWrongName() {
        //given
        final Context context = RuntimeEnvironment.application;
        //when
        projectsActivity.showWrongNameError();
        //then
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(context.getString(R.string.wrong_project_name));
    }

    @Test
    public void longClickShouldStartSelectionModeAndSelectItem() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        RecyclerView recyclerView = projectsActivity.projectsRecyclerView;
        ProjectAdapter adapter = (ProjectAdapter) recyclerView.getAdapter();
        ExpenseProject firstProject = new ExpenseProject("First project", defaultCurrency);
        ExpenseProject secondProject = new ExpenseProject("Second project", defaultCurrency);
        //when
        projectsActivity.showProjects(Arrays.asList(firstProject, secondProject));
        projectsActivity.onItemLongClicked(1);
        //then
        assertThat(projectsActivity.actionMode).isNotNull();
        assertThat(adapter.getSelectedItemCount()).isEqualTo(1);
        assertThat(adapter.getSelectedItems()).isNotEmpty().containsOnly(1);
    }

    @Test
    public void shortClickShouldSelectItemInSelectionMode() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        RecyclerView recyclerView = projectsActivity.projectsRecyclerView;
        ProjectAdapter adapter = (ProjectAdapter) recyclerView.getAdapter();
        ExpenseProject firstProject = new ExpenseProject("First project", defaultCurrency);
        ExpenseProject secondProject = new ExpenseProject("Second project", defaultCurrency);
        //when
        projectsActivity.showProjects(Arrays.asList(firstProject, secondProject));
        projectsActivity.onItemLongClicked(0);
        projectsActivity.onItemClicked(1);
        //then
        assertThat(projectsActivity.actionMode).isNotNull();
        assertThat(adapter.getSelectedItemCount()).isEqualTo(2);
        assertThat(adapter.getSelectedItems()).isNotEmpty().containsOnly(0, 1);
    }

    @Test
    public void shouldShowDeleteButtonWhenInSelectionMode() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject firstProject = new ExpenseProject("First project", defaultCurrency);
        //when
        projectsActivity.showProjects(Collections.singletonList(firstProject));
        projectsActivity.onItemLongClicked(0);
        MenuItem deleteItem = projectsActivity.actionMode.getMenu().findItem(R.id.menu_remove);
        //then
        assertThat(deleteItem).isNotNull();
    }

    @Test
    public void shouldCallPresenterWhileRemovingItems() {
        //given
        Currency defaultCurrency = new Currency("EUR");
        ExpenseProject project = new ExpenseProject("First project", defaultCurrency);
        Map<Integer, ExpenseProject> expectedArgument = new HashMap<>();
        expectedArgument.put(0, project);
        //when
        projectsActivity.showProjects(Collections.singletonList(project));
        projectsActivity.onItemLongClicked(0);
        MenuItem deleteItem = projectsActivity.actionMode.getMenu().findItem(R.id.menu_remove);
        projectsActivity.onActionItemClicked(projectsActivity.actionMode, deleteItem);
        //then
        verify(presenterModuleMock.getProjectsPresenter()).removeProjects(expectedArgument);
    }

    @Test
    public void shouldRemoveExpensesFromView() {
        //given
        int itemPosition = 0;
        Currency defaultCurrency = new Currency("EUR");
        RecyclerView recyclerView = projectsActivity.projectsRecyclerView;
        ProjectAdapter adapter = (ProjectAdapter) recyclerView.getAdapter();
        Set<Integer> positions = new HashSet<>();
        positions.add(itemPosition);
        ExpenseProject project = new ExpenseProject("First project", defaultCurrency);
        //when
        projectsActivity.showProjects(Collections.singletonList(project));
        projectsActivity.onItemLongClicked(0);
        projectsActivity.removeProjects(positions);
        //then
        assertThat(adapter.getSelectedItemCount()).isEqualTo(0);
        assertThat(adapter.getSelectedItems()).isEmpty();
        assertThat(adapter.getItemCount()).isEqualTo(0);
        assertThat(adapter.getItems()).isEmpty();
    }

    @Test
    public void shouldLoadProjectsUponActivityStart() {
        //then
        verify(presenterModuleMock.getProjectsPresenter()).loadProjects();
    }
}

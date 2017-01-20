package costy.krzysiekz.com.costy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

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
@Config(constants = BuildConfig.class, sdk = 23,
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
        //when
        projectsActivity.onProjectNameConfirmed(projectName);
        //then
        verify(presenterModuleMock.getProjectsPresenter()).addProject(projectName);
    }

    @Test
    public void shouldShowProjects() {
        //given
        ExpenseProject project1 = new ExpenseProject("Project 1");
        ExpenseProject project2 = new ExpenseProject("Project 2");
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
        ExpenseProject project1 = new ExpenseProject("Project 1");
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
}

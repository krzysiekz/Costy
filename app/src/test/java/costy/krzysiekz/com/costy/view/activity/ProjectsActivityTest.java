package costy.krzysiekz.com.costy.view.activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.view.ProjectsView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
        projectsActivity = Robolectric.buildActivity(ProjectsActivity.class).create().get();
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
        assertThat(projectsActivity.getPresenter()).isNotNull();
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getProjectsPresenter()).attachView(projectsActivity);
    }
}

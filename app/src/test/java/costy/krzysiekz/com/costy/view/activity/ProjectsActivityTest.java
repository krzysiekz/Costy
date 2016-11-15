package costy.krzysiekz.com.costy.view.activity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.presenter.impl.ProjectsPresenter;
import costy.krzysiekz.com.costy.view.ProjectsView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ProjectsActivityTest {

    private ActivityController<ProjectsActivity> projectsActivityController;

    @Before
    public void setUp() throws Exception {
        //given
        projectsActivityController = Robolectric.buildActivity(ProjectsActivity.class);
    }

    @Test
    public void shouldExist() {
        //when
        ProjectsActivity activity = projectsActivityController.create().get();
        //then
        assertThat(activity).isNotNull();
    }

    @Test
    public void shouldImplementProjectsView() {
        //when
        ProjectsActivity activity = projectsActivityController.create().get();
        //then
        assertThat(activity).isInstanceOf(ProjectsView.class);
    }

    @Test
    @Ignore
    public void shouldAttachViewToThePresenter() {
        //given
        ProjectsActivity activity = projectsActivityController.get();
        ProjectsPresenter presenter = mock(ProjectsPresenter.class);
        //when
        activity.setPresenter(presenter);
        this.projectsActivityController.create();
        //then
        verify(presenter).attachView(activity);
    }
}

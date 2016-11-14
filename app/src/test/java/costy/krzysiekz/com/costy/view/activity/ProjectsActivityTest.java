package costy.krzysiekz.com.costy.view.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import costy.krzysiekz.com.costy.BuildConfig;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class ProjectsActivityTest {

    @Test
    public void shouldExist() {
        //given
        //when
        ProjectsActivity projectsActivity =
                Robolectric.buildActivity(ProjectsActivity.class).create().get();
        //then
        assertThat(projectsActivity).isNotNull();
    }
}

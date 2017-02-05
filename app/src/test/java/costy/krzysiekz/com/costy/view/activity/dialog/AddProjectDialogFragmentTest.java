package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24)
public class AddProjectDialogFragmentTest {

    private ProjectsActivity projectsActivity;
    private AddProjectDialogFragment fragment;
    private AlertDialog dialog;

    @Before
    public void setUp() throws Exception {
        //given
        projectsActivity = Robolectric.buildActivity(ProjectsActivity.class).
                create().start().resume().get();
        fragment = new AddProjectDialogFragment();
        fragment.show(projectsActivity.getSupportFragmentManager(), AddProjectDialogFragment.TAG);
        dialog = (AlertDialog) fragment.getDialog();

    }

    @Test
    public void shouldHaveListenerSet() {
        //when
        AddProjectDialogListener dialogListener = fragment.getListenerActivity();
        //then
        assertThat(dialogListener).isNotNull();
        assertThat(dialogListener).isEqualTo(projectsActivity);
    }

    @Test
    public void shouldShowDialog() {
        //then
        assertThat(dialog).isNotNull();
        assertThat(dialog).isInstanceOf(AlertDialog.class);
    }

    @Test
    public void shouldCallListenerWithProperValueWhenUserClickOk() {
        //given
        String projectName = "Sample project";
        ProjectsActivity projectsActivityMock = mock(ProjectsActivity.class);
        //when
        fragment.setListenerActivity(projectsActivityMock);
        EditText projectNameText = (EditText) dialog.findViewById(R.id.project_name);
        projectNameText.setText(projectName);
        //then
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(projectsActivityMock).onProjectNameConfirmed(projectName);
    }

    @Test
    public void shouldCallListenerWhenNameEmpty() {
        //given
        String projectName = "";
        ProjectsActivity projectsActivityMock = mock(ProjectsActivity.class);
        //when
        fragment.setListenerActivity(projectsActivityMock);
        EditText projectNameText = (EditText) dialog.findViewById(R.id.project_name);
        projectNameText.setText(projectName);
        //then
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(projectsActivityMock).showWrongNameError();
    }
}
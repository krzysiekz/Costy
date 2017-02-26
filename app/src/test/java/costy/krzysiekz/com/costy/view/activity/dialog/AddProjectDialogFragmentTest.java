package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

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
        List<Currency> currencies = Arrays.asList(new Currency("EUR"), new Currency("PLN"));
        fragment.setCurrencies(currencies);
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
    public void shouldPopulateSpinnerWithListOfCurrencies() {
        //given
        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_project_dialog_default_currency);
        //when
        SpinnerAdapter currencySpinnerAdapter = payerSpinner.getAdapter();
        //then
        assertThat(currencySpinnerAdapter.getCount()).isEqualTo(2);
        assertThat(currencySpinnerAdapter.getItem(0)).isEqualTo("EUR");
        assertThat(currencySpinnerAdapter.getItem(1)).isEqualTo("PLN");
    }

    @Test
    public void shouldCallListenerWithProperValueWhenUserClickOk() {
        //given
        String projectName = "Sample project";
        ExpenseProject expectedProject = new ExpenseProject(projectName, new Currency("PLN"));
        ProjectsActivity projectsActivityMock = mock(ProjectsActivity.class);

        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_project_dialog_default_currency);
        EditText projectNameText = (EditText) dialog.findViewById(R.id.project_name);
        //when
        fragment.setListenerActivity(projectsActivityMock);
        projectNameText.setText(projectName);
        currencySpinner.setSelection(1);
        //then
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(projectsActivityMock).onProjectConfirmed(expectedProject);
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
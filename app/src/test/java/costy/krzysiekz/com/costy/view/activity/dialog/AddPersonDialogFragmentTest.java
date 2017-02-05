package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24)
public class AddPersonDialogFragmentTest {

    private AddPersonDialogFragment fragment;
    private AlertDialog dialog;
    private PeopleFragment peopleFragment;

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        SelectedProjectActivity activity = Robolectric.buildActivity(SelectedProjectActivity.class).
                create().start().resume().get();
        fragment = new AddPersonDialogFragment();
        peopleFragment = mock(PeopleFragment.class);
        fragment.setListener(peopleFragment);
        fragment.show(activity.getSupportFragmentManager(), AddPersonDialogFragment.TAG);
        dialog = (AlertDialog) fragment.getDialog();
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        app.setPresenterModule(new PresenterModuleMock());
    }

    @Test
    public void shouldHaveListenerSet() {
        //when
        AddPersonDialogListener dialogListener = fragment.getListener();
        //then
        assertThat(dialogListener).isNotNull();
        assertThat(dialogListener).isEqualTo(peopleFragment);
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
        String personName = "John";
        PeopleFragment peopleFragment = mock(PeopleFragment.class);
        //when
        fragment.setListener(peopleFragment);
        EditText personNameText = (EditText) dialog.findViewById(R.id.person_name);
        personNameText.setText(personName);
        //then
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(peopleFragment).onPersonNameConfirmed(personName);
    }

    @Test
    public void shouldCallListenerWhenNameEmpty() {
        //given
        String personName = "";
        PeopleFragment peopleFragment = mock(PeopleFragment.class);
        //when
        fragment.setListener(peopleFragment);
        EditText personNameText = (EditText) dialog.findViewById(R.id.person_name);
        personNameText.setText(personName);
        //then
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        verify(peopleFragment).showWrongNameError();
    }

}
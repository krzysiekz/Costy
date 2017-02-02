package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.fragment.ExpensesFragment;
import costy.krzysiekz.com.costy.view.utils.MultiSelectSpinner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class AddExpenseDialogFragmentTest {

    private AddExpenseDialogFragment fragment;
    private AlertDialog dialog;
    private ExpensesFragment expensesFragment;

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        SelectedProjectActivity activity = Robolectric.buildActivity(SelectedProjectActivity.class).
                create().start().resume().get();
        fragment = new AddExpenseDialogFragment();

        expensesFragment = mock(ExpensesFragment.class);
        fragment.setUsers(createUsers());

        fragment.setListener(expensesFragment);
        fragment.show(activity.getSupportFragmentManager(), AddExpenseDialogFragment.TAG);
        dialog = (AlertDialog) fragment.getDialog();
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        app.setPresenterModule(new PresenterModuleMock());
    }

    @Test
    public void shouldHaveListenerSet() {
        //when
        AddExpenseDialogListener dialogListener = fragment.getListener();
        //then
        assertThat(dialogListener).isNotNull();
        assertThat(dialogListener).isEqualTo(expensesFragment);
    }

    @Test
    public void shouldShowDialog() {
        //then
        assertThat(dialog).isNotNull();
        assertThat(dialog).isInstanceOf(AlertDialog.class);
    }

    @Test
    public void shouldPopulatePayerSpinnerWithListOfUsers() {
        //given
        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_expense_dialog_from);
        //when
        SpinnerAdapter payerSpinnerAdapter = payerSpinner.getAdapter();
        //then
        assertThat(payerSpinnerAdapter.getCount()).isEqualTo(2);
        assertThat(payerSpinnerAdapter.getItem(0)).isEqualTo("Kate");
        assertThat(payerSpinnerAdapter.getItem(1)).isEqualTo("John");
    }

    @Test
    public void shouldPopulateReceiverSpinnerWithListOfUsers() {
        //given
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);
        //when
        List<String> items = receiversSpinner.getItems();
        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(0)).isEqualTo("Kate");
        assertThat(items.get(1)).isEqualTo("John");
    }

    @Test
    public void shouldHaveAllReceiversCheckedByDefault() {
        //given
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);
        //when
        boolean[] selected = receiversSpinner.getSelected();
        //then
        for (boolean sel : selected) {
            assertThat(sel).isTrue();
        }
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();
        User kate = new User("Kate");
        User john = new User("John");
        users.add(kate);
        users.add(john);
        return users;
    }
}
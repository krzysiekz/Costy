package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24)
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
        fragment.setCurrencies(createCurrencies());
        fragment.setDefaultCurrency(new Currency("EUR"));

        fragment.setListener(expensesFragment);
        fragment.show(activity.getSupportFragmentManager(), AddExpenseDialogFragment.TAG);
        dialog = (AlertDialog) fragment.getDialog();
    }

    private List<Currency> createCurrencies() {
        return Arrays.asList(new Currency("PLN"), new Currency("EUR"));
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

    @Test
    public void shouldAddExpenseWithProperAmountAndDescriptionWhenAllReceiversSelected() {
        //given
        String sampleAmount = "10.50";
        String sampleDescription = "This is sample description";

        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_expense_dialog_from);
        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_expense_currency);
        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        ArgumentCaptor<UserExpense> expenseCaptor = ArgumentCaptor.forClass(UserExpense.class);
        //when
        payerSpinner.setSelection(1);
        currencySpinner.setSelection(0);
        amount.setText(sampleAmount);
        description.setText(sampleDescription);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        //then
        verify(expensesFragment).onExpenseConfirmed(expenseCaptor.capture());
        assertThat(expenseCaptor.getValue().getUser().getName()).isEqualTo("John");
        assertThat(expenseCaptor.getValue().getAmount()).isEqualTo(new BigDecimal(sampleAmount));
        assertThat(expenseCaptor.getValue().getDescription()).isEqualTo(sampleDescription);
        assertThat(expenseCaptor.getValue().getReceivers()).hasSize(2).extracting("name").contains("John", "Kate");
        assertThat(expenseCaptor.getValue().getCurrency()).extracting(Currency::getName).contains("PLN");
    }

    @Test
    public void shouldAddExpenseWithProperAmountAndDescriptionWhenOneReceiverSelected() {
        //given
        String sampleAmount = "10.50";
        String sampleDescription = "This is sample description";
        int johnItemIndex = 1;

        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_expense_dialog_from);
        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);
        ArgumentCaptor<UserExpense> expenseCaptor = ArgumentCaptor.forClass(UserExpense.class);
        //when
        payerSpinner.setSelection(1);
        amount.setText(sampleAmount);
        description.setText(sampleDescription);
        receiversSpinner.performClick();
        AlertDialog receiversSelection = ShadowAlertDialog.getLatestAlertDialog();
        receiversSelection.getListView().performItemClick(
                receiversSelection.getListView().getAdapter().getView(johnItemIndex, null, null), johnItemIndex, johnItemIndex);
        receiversSelection.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        //then
        verify(expensesFragment).onExpenseConfirmed(expenseCaptor.capture());
        assertThat(expenseCaptor.getValue().getUser().getName()).isEqualTo("John");
        assertThat(expenseCaptor.getValue().getAmount()).isEqualTo(new BigDecimal(sampleAmount));
        assertThat(expenseCaptor.getValue().getDescription()).isEqualTo(sampleDescription);
        assertThat(expenseCaptor.getValue().getReceivers()).hasSize(1).extracting("name").contains("Kate");
    }

    @Test
    public void shouldShowErrorWhenDescriptionNotProvided() {
        //given
        String sampleAmount = "10.50";
        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        //when
        amount.setText(sampleAmount);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        //then
        assertThat(dialog.isShowing()).isTrue();
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).
                isEqualTo(fragment.getString(R.string.add_expense_provide_description_error));
    }

    @Test
    public void shouldShowErrorWhenAmountNotProvided() {
        //given
        String sampleDescription = "This is sample description";
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        //when
        description.setText(sampleDescription);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        //then
        assertThat(dialog.isShowing()).isTrue();
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).
                isEqualTo(fragment.getString(R.string.add_expense_provide_amount_error));
    }

    @Test
    public void shouldShowErrorWhenReceiversNotSelected() {
        //given
        String sampleAmount = "10.50";
        String sampleDescription = "This is sample description";
        int johnItemIndex = 1;
        int kateItemIndex = 0;

        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);
        //when
        amount.setText(sampleAmount);
        description.setText(sampleDescription);
        receiversSpinner.performClick();
        AlertDialog receiversSelection = ShadowAlertDialog.getLatestAlertDialog();
        receiversSelection.getListView().performItemClick(
                receiversSelection.getListView().getAdapter().getView(johnItemIndex, null, null), johnItemIndex, johnItemIndex);
        receiversSelection.getListView().performItemClick(
                receiversSelection.getListView().getAdapter().getView(kateItemIndex, null, null), kateItemIndex, kateItemIndex);
        receiversSelection.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        //then
        assertThat(dialog.isShowing()).isTrue();
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).
                isEqualTo(fragment.getString(R.string.add_expense_select_receivers_error));
    }

    @Test
    public void shouldPopulateCurrencySpinnerWithListOfSupportedCurrencies() {
        //given
        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_expense_currency);
        //when
        SpinnerAdapter currencySpinnerAdapter = currencySpinner.getAdapter();
        //then
        assertThat(currencySpinnerAdapter.getCount()).isEqualTo(2);
        assertThat(currencySpinnerAdapter.getItem(0)).isEqualTo("PLN");
        assertThat(currencySpinnerAdapter.getItem(1)).isEqualTo("EUR");
    }

    @Test
    public void shouldSetDefaultCurrencyInSpinner() {
        //given
        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_expense_currency);
        //when
        String selected = (String) currencySpinner.getSelectedItem();
        //then
        assertThat(selected).isEqualTo("EUR");
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
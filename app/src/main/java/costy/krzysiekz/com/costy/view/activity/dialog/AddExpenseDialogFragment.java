package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.utils.MultiSelectSpinner;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddExpenseDialogFragment extends DialogFragment {

    public static final String TAG = "ADD_PERSON_DIALOG";
    public static final String STATE_USERS = "STATE_USERS";
    public static final String STATE_CURRENCIES = "STATE_CURRENCIES";
    public static final String STATE_DEFAULT_CURRENCY = "STATE_DEFAULT_CURRENCY";

    private AddExpenseDialogListener listener;
    private List<User> users;
    private List<Currency> currencies;
    private Currency defaultCurrency;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.dialog_add_expense, null);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_USERS)) {
            users = getUsersState(savedInstanceState);
            currencies = getCurrenciesState(savedInstanceState);
            defaultCurrency = getDefaultCurrencyState(savedInstanceState);
        }

        List<String> userNames = StreamSupport.stream(users).map(User::getName).collect(Collectors.toList());

        setUpPayerSpinner(inflate, userNames);
        setUpReceiversSpinner(inflate, userNames);
        setUpCurrencySpinner(inflate);

        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> positiveButtonClicked(dialog));
        });


        return dialog;
    }

    @SuppressWarnings("unchecked")
    private List<User> getUsersState(Bundle savedInstanceState) {
        return (List<User>) savedInstanceState.getSerializable(STATE_USERS);
    }

    @SuppressWarnings("unchecked")
    private List<Currency> getCurrenciesState(Bundle savedInstanceState) {
        return (List<Currency>) savedInstanceState.getSerializable(STATE_CURRENCIES);
    }

    private Currency getDefaultCurrencyState(Bundle savedInstanceState) {
        return (Currency) savedInstanceState.getSerializable(STATE_DEFAULT_CURRENCY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_USERS, new ArrayList<>(users));
        outState.putSerializable(STATE_CURRENCIES, new ArrayList<>(currencies));
        outState.putSerializable(STATE_DEFAULT_CURRENCY, defaultCurrency);
        super.onSaveInstanceState(outState);
    }

    private void positiveButtonClicked(Dialog dialog) {
        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_expense_dialog_from);
        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);
        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_expense_currency);

        List<User> receivers = getReceivers(receiversSpinner.getSelected());

        if (amount.getText().length() == 0) {
            showToast(R.string.add_expense_provide_amount_error);
        } else if (description.getText().length() == 0) {
            showToast(R.string.add_expense_provide_description_error);
        } else if (receivers.isEmpty()) {
            showToast(R.string.add_expense_select_receivers_error);
        } else {
            User payer = users.get(payerSpinner.getSelectedItemPosition());
            Currency currency = currencies.get(currencySpinner.getSelectedItemPosition());

            UserExpense expense = new UserExpense.UserExpenseBuilder().
                    withUser(payer).
                    withAmount(new BigDecimal(amount.getText().toString())).
                    withReceivers(receivers).
                    withDescription(description.getText().toString()).
                    withCurrency(currency).build();

            listener.onExpenseConfirmed(expense);
            dialog.dismiss();
        }
    }

    private void showToast(int messageId) {
        Toast.makeText(this.getContext(), messageId, Toast.LENGTH_SHORT).show();
    }

    private List<User> getReceivers(boolean[] selected) {
        List<User> receivers = new ArrayList<>();

        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                receivers.add(users.get(i));
            }
        }
        return receivers;
    }

    private void setUpReceiversSpinner(View inflate, List<String> userNames) {
        MultiSelectSpinner multiSpinner = (MultiSelectSpinner) inflate.findViewById(R.id.add_expense_receivers);
        multiSpinner.setItems(userNames, getString(R.string.add_expense_text_when_receivers_selected));
        multiSpinner.setSpinnerTitle(getString(R.string.add_expense_select_receivers_dialog_title));
    }

    private void setUpPayerSpinner(View inflate, List<String> userNames) {
        Spinner single = (Spinner) inflate.findViewById(R.id.add_expense_dialog_from);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, userNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        single.setAdapter(dataAdapter);
    }

    private void setUpCurrencySpinner(View view) {
        List<String> currencyNames = StreamSupport.stream(currencies).map(Currency::getName).collect(Collectors.toList());
        Spinner single = (Spinner) view.findViewById(R.id.add_expense_currency);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, currencyNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        single.setAdapter(dataAdapter);
        single.setSelection(currencies.indexOf(defaultCurrency));
    }

    public void setListener(AddExpenseDialogListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    AddExpenseDialogListener getListener() {
        return listener;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}

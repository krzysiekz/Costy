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
    private AddExpenseDialogListener listener;
    private List<User> users;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.dialog_add_expense, null);

        List<String> userNames = StreamSupport.stream(users).map(User::getName).collect(Collectors.toList());

        setUpPayerSpinner(inflate, userNames);
        setUpReceiversSpinner(inflate, userNames);

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

    private void positiveButtonClicked(Dialog dialog) {
        Spinner payerSpinner = (Spinner) dialog.findViewById(R.id.add_expense_dialog_from);
        EditText amount = (EditText) dialog.findViewById(R.id.add_expense_amount);
        EditText description = (EditText) dialog.findViewById(R.id.add_expense_description);
        MultiSelectSpinner receiversSpinner = (MultiSelectSpinner) dialog.findViewById(R.id.add_expense_receivers);

        List<User> receivers = getReceivers(receiversSpinner.getSelected());

        if (amount.getText().length() == 0) {
            showToast(R.string.add_expense_provide_amount_error);
        } else if (description.getText().length() == 0) {
            showToast(R.string.add_expense_provide_description_error);
        } else if (receivers.isEmpty()) {
            showToast(R.string.add_expense_select_receivers_error);
        } else {
            User payer = users.get(payerSpinner.getSelectedItemPosition());

            UserExpense expense = new UserExpense(payer, new BigDecimal(amount.getText().toString()),
                    receivers, description.getText().toString());
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

    public void setListener(AddExpenseDialogListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    AddExpenseDialogListener getListener() {
        return listener;
    }
}

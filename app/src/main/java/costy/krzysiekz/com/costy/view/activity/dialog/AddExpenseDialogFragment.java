package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.krzysiekz.costy.model.User;

import java.util.List;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.utils.MultiSelectSpinner;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddExpenseDialogFragment extends DialogFragment implements MultiSelectSpinner.MultiSpinnerListener {

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

        builder.setView(inflate);
        return builder.create();
    }

    private void setUpReceiversSpinner(View inflate, List<String> userNames) {
        MultiSelectSpinner multiSpinner = (MultiSelectSpinner) inflate.findViewById(R.id.add_expense_receivers);
        multiSpinner.setItems(userNames, getString(R.string.add_expense_text_when_receivers_selected), this);
        multiSpinner.setSpinnerTitle(getString(R.string.add_expense_select_receivers_dialog_title));
    }

    private void setUpPayerSpinner(View inflate, List<String> userNames) {
        Spinner single = (Spinner) inflate.findViewById(R.id.add_expense_dialog_from);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, userNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        single.setAdapter(dataAdapter);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {

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

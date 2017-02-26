package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class AddProjectDialogFragment extends DialogFragment {

    public static final String TAG = "ADD_PROJECT_DIALOG";
    private static final String STATE_CURRENCIES = "STATE_CURRENCIES";

    private AddProjectDialogListener addProjectDialogListener;
    private List<Currency> currencies;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddProjectDialogListener) {
            this.addProjectDialogListener = (AddProjectDialogListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_project, null);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENCIES)) {
            currencies = getCurrenciesState(savedInstanceState);
        }

        builder.setView(view).setPositiveButton(R.string.confirm_project_name, (dialog, id) -> positiveButtonClicked((Dialog) dialog));
        builder.setNegativeButton(android.R.string.cancel, null);

        setUpCurrencySpinner(view);
        return builder.create();
    }

    @SuppressWarnings("unchecked")
    private List<Currency> getCurrenciesState(Bundle savedInstanceState) {
        return (List<Currency>) savedInstanceState.getSerializable(STATE_CURRENCIES);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_CURRENCIES, new ArrayList<>(currencies));
        super.onSaveInstanceState(outState);
    }

    private void setUpCurrencySpinner(View view) {
        List<String> currencyNames = StreamSupport.stream(currencies).map(Currency::getName).collect(Collectors.toList());
        Spinner single = (Spinner) view.findViewById(R.id.add_project_dialog_default_currency);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, currencyNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        single.setAdapter(dataAdapter);
    }

    private void positiveButtonClicked(Dialog dialog) {
        EditText projectNameText = (EditText) dialog.findViewById(R.id.project_name);
        Spinner currencySpinner = (Spinner) dialog.findViewById(R.id.add_project_dialog_default_currency);

        if (projectNameText.length() > 0) {
            Currency defaultCurrency = currencies.get(currencySpinner.getSelectedItemPosition());
            ExpenseProject project = new ExpenseProject((projectNameText).getText().toString(), defaultCurrency);
            addProjectDialogListener.
                    onProjectConfirmed(project);
        } else {
            addProjectDialogListener.showWrongNameError();
        }
    }

    AddProjectDialogListener getListenerActivity() {
        return addProjectDialogListener;
    }

    void setListenerActivity(ProjectsActivity listenerActivity) {
        this.addProjectDialogListener = listenerActivity;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }
}

package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;

public class AddPersonDialogFragment extends DialogFragment {

    public static final String TAG = "ADD_PERSON_DIALOG";
    private AddPersonDialogListener addPersonDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_add_person, null))
                .setPositiveButton(R.string.confirm_project_name, (dialog, id) -> positiveButtonClicked((Dialog) dialog));
        return builder.create();
    }

    private void positiveButtonClicked(Dialog dialog) {
        EditText personNameText = (EditText) dialog.findViewById(R.id.person_name);
        if (personNameText.length() > 0) {
            addPersonDialogListener.onPersonNameConfirmed((personNameText).getText().toString());
        } else {
            addPersonDialogListener.showWrongNameError();
        }
    }

    AddPersonDialogListener getListener() {
        return addPersonDialogListener;
    }

    public void setListener(PeopleFragment activity) {
        this.addPersonDialogListener = activity;
    }
}

package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.krzysiekz.costy.model.Currency;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;

public class AddProjectDialogFragment extends DialogFragment {

    public static final String TAG = "ADD_PROJECT_DIALOG";

    private AddProjectDialogListener addProjectDialogListener;

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
        builder.setView(inflater.inflate(R.layout.dialog_add_project, null))
                .setPositiveButton(R.string.confirm_project_name, (dialog, id) -> positiveButtonClicked((Dialog) dialog));
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    private void positiveButtonClicked(Dialog dialog) {
        EditText projectNameText = (EditText) dialog.findViewById(R.id.project_name);
        if (projectNameText.length() > 0) {
            //TODO implement adding default currency
            Currency defaultCurrency = null;
            addProjectDialogListener.
                    onProjectNameConfirmed((projectNameText).getText().toString(), defaultCurrency);
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
}

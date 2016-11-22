package costy.krzysiekz.com.costy.view.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

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
                .setPositiveButton(R.string.confirm_project_name, (dialog, id) -> {
                    EditText projectNameText = (EditText) ((Dialog) dialog).findViewById(R.id.project_name);
                    addProjectDialogListener.onProjectNameConfirmed((projectNameText).getText().toString());
                });
        return builder.create();
    }

    AddProjectDialogListener getListenerActivity() {
        return addProjectDialogListener;
    }

    void setListenerActivity(ProjectsActivity listenerActivity) {
        this.addProjectDialogListener = listenerActivity;
    }
}
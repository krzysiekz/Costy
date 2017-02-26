package costy.krzysiekz.com.costy.view.activity.dialog;


import com.krzysiekz.costy.model.ExpenseProject;

public interface AddProjectDialogListener {
    void onProjectConfirmed(ExpenseProject project);

    void showWrongNameError();
}

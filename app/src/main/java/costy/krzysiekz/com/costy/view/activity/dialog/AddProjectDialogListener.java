package costy.krzysiekz.com.costy.view.activity.dialog;


public interface AddProjectDialogListener {
    void onProjectNameConfirmed(String projectName);

    void showWrongNameError();
}

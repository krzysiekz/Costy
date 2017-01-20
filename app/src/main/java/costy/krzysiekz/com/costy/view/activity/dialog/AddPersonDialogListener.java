package costy.krzysiekz.com.costy.view.activity.dialog;


public interface AddPersonDialogListener {
    void onPersonNameConfirmed(String personName);

    void showWrongNameError();
}

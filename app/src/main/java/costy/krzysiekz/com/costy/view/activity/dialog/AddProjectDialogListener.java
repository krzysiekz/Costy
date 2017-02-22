package costy.krzysiekz.com.costy.view.activity.dialog;


import com.krzysiekz.costy.model.Currency;

public interface AddProjectDialogListener {
    void onProjectNameConfirmed(String projectName, Currency currency);

    void showWrongNameError();
}

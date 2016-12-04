package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;

public class ExpensesActivity extends AppCompatActivity implements ExpensesView {

    public static final String PROJECT_NAME = "PROJECT_NAME";
    ExpensesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        CostyApplication.component().inject(this);
        presenter.attachView(this);

    }

    @Inject
    void setPresenter(ExpensesPresenter presenter) {
        this.presenter = presenter;
    }

}

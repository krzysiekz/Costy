package costy.krzysiekz.com.costy.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.UserExpense;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;

public class ExpensesActivity extends AppCompatActivity implements ExpensesView {

    public static final String PROJECT_NAME = "PROJECT_NAME";

    ExpensesPresenter presenter;

    @BindView(R.id.expenses_recycler_view)
    RecyclerView expensesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        CostyApplication.component().inject(this);
        ButterKnife.bind(this);

        setupExpensesRecycleView();

        presenter.attachView(this);
        presenter.loadProjectExpenses(getIntent().getStringExtra(PROJECT_NAME));
    }

    private void setupExpensesRecycleView() {
        ExpensesAdapter adapter = new ExpensesAdapter();
        expensesRecyclerView.setAdapter(adapter);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Inject
    void setPresenter(ExpensesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showExpenses(List<UserExpense> expenses) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.setExpenses(expenses);
        adapter.notifyDataSetChanged();
    }
}

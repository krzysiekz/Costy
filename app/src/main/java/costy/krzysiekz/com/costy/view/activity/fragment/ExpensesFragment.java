package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class ExpensesFragment extends Fragment implements ExpensesView {

    ExpensesPresenter presenter;

    @BindView(R.id.expenses_recycler_view)
    RecyclerView expensesRecyclerView;

    public ExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupExpensesRecycleView();

        presenter.attachView(this);
        presenter.loadProjectExpenses(getArguments().getString(PROJECT_NAME));

        return view;
    }

    private void setupExpensesRecycleView() {
        ExpensesAdapter adapter = new ExpensesAdapter();
        expensesRecyclerView.setAdapter(adapter);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showExpenses(List<UserExpense> expenses) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.setExpenses(expenses);
        adapter.notifyDataSetChanged();
    }

    @Inject
    void setPresenter(ExpensesPresenter presenter) {
        this.presenter = presenter;
    }

}

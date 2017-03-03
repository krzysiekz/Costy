package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.SelectableView;
import costy.krzysiekz.com.costy.view.activity.SelectableViewHandler;
import costy.krzysiekz.com.costy.view.activity.adapter.ClickListener;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;
import costy.krzysiekz.com.costy.view.activity.adapter.SelectableAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddExpenseDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddExpenseDialogListener;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class ExpensesFragment extends Fragment
        implements ExpensesView, AddExpenseDialogListener, SelectableView<UserExpense>, ClickListener {

    ActionMode actionMode;
    private String projectName;

    ExpensesPresenter presenter;

    @BindView(R.id.expenses_recycler_view)
    RecyclerView expensesRecyclerView;

    @BindView(R.id.add_expense_button)
    FloatingActionButton addExpenseButton;

    SelectableViewHandler<UserExpense> selectableViewHandler;

    public ExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        setupExpensesRecycleView();
        selectableViewHandler = new SelectableViewHandler<>(this);

        projectName = getArguments().getString(PROJECT_NAME);

        presenter.attachView(this);
        presenter.loadProjectExpenses(projectName);

        addExpenseButton.setOnClickListener(__ -> presenter.showAddExpenseDialog(projectName));

        if (savedInstanceState != null) {
            AddExpenseDialogFragment dialogFragment = (AddExpenseDialogFragment) getFragmentManager().
                    findFragmentByTag(AddExpenseDialogFragment.TAG);
            if (dialogFragment != null) {
                dialogFragment.setListener(this);
            }
        }

        return view;
    }

    private void setupExpensesRecycleView() {
        ExpensesAdapter adapter = new ExpensesAdapter(this);
        expensesRecyclerView.setAdapter(adapter);
        expensesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showExpenses(List<UserExpense> expenses) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.setItems(expenses);
        adapter.notifyDataSetChanged();
    }

    @Inject
    void setPresenter(ExpensesPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAddExpenseDialog(List<User> people, List<Currency> supportedCurrencies,
                                     Currency defaultCurrency) {
        AddExpenseDialogFragment dialogFragment = new AddExpenseDialogFragment();
        dialogFragment.setUsers(people);
        dialogFragment.setCurrencies(supportedCurrencies);
        dialogFragment.setDefaultCurrency(defaultCurrency);
        dialogFragment.setListener(this);
        dialogFragment.show(getFragmentManager(), AddExpenseDialogFragment.TAG);
    }

    @Override
    public void onExpenseConfirmed(UserExpense expense) {
        presenter.addExpense(projectName, expense);
    }


    @Override
    public void removeExpenses(Set<Integer> expensesPositions) {
        selectableViewHandler.removeItems(expensesPositions);
    }

    @Override
    public SelectableAdapter<?, UserExpense> getAdapter() {
        return (ExpensesAdapter) expensesRecyclerView.getAdapter();
    }

    @Override
    public void handleRemoveButtonClicked(Map<Integer, UserExpense> selectedItemObjects) {
        presenter.removeExpenses(projectName, selectedItemObjects);
    }

    @Override
    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    @Override
    public ActionMode getActionMode() {
        return actionMode;
    }

    @Override
    public void handleSingleItemClick(UserExpense clickedItem) {
    }

    @Override
    public ActionMode startSupportActionMode() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        return activity.startSupportActionMode(selectableViewHandler);
    }

    @Override
    public void onItemClicked(int position) {
        selectableViewHandler.onItemClicked(position);
    }

    @Override
    public boolean onItemLongClicked(int position) {
        return selectableViewHandler.onItemLongClicked(position);
    }
}

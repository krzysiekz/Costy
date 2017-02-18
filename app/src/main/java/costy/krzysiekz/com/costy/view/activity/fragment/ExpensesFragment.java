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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.activity.adapter.ClickListener;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddExpenseDialogFragment;
import costy.krzysiekz.com.costy.view.activity.dialog.AddExpenseDialogListener;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class ExpensesFragment extends Fragment
        implements ExpensesView, AddExpenseDialogListener, ClickListener, ActionMode.Callback {

    ActionMode actionMode;
    private String projectName;

    ExpensesPresenter presenter;

    @BindView(R.id.expenses_recycler_view)
    RecyclerView expensesRecyclerView;

    @BindView(R.id.add_expense_button)
    FloatingActionButton addExpenseButton;

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
    public void showAddExpenseDialog(List<User> people) {
        AddExpenseDialogFragment dialogFragment = new AddExpenseDialogFragment();
        dialogFragment.setUsers(people);
        dialogFragment.setListener(this);
        dialogFragment.show(getFragmentManager(), AddExpenseDialogFragment.TAG);
    }

    @Override
    public void onExpenseConfirmed(UserExpense expense) {
        presenter.addExpense(projectName, expense);
    }

    @Override
    public void onItemClicked(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        }
    }

    @Override
    public boolean onItemLongClicked(int position) {
        if (actionMode == null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionMode = activity.startSupportActionMode(this);
        }

        toggleSelection(position);
        return true;
    }

    private void toggleSelection(int position) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        switch (item.getItemId()) {
            case R.id.menu_remove:
                presenter.removeExpenses(projectName, adapter.getSelectedItemObjects());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.clearSelection();
        actionMode = null;
    }

    @Override
    public void removeExpenses(Set<Integer> expensesPositions) {
        ExpensesAdapter adapter = (ExpensesAdapter) expensesRecyclerView.getAdapter();
        adapter.removeItems(new ArrayList<>(expensesPositions));
        actionMode.finish();
    }
}

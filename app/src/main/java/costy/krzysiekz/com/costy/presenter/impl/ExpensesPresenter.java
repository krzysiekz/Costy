package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.List;
import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.ExpensesView;

public class ExpensesPresenter implements Presenter<ExpensesView> {

    private ProjectsRepository repository;
    private ExpensesView view;

    public ExpensesPresenter(ProjectsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(ExpensesView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadProjectExpenses(String projectName) {
        List<UserExpense> expenses = repository.getAllExpenses(projectName);
        view.showExpenses(expenses);
    }

    public void showAddExpenseDialog(String projectName) {
        ExpenseProject project = this.repository.getProject(projectName);
        List<User> users = project.getUsers();
        view.showAddExpenseDialog(users, repository.getAllCurrencies(), project.getDefaultCurrency());
    }

    public void addExpense(String projectName, UserExpense expense) {
        repository.addExpense(projectName, expense);
        view.showExpenses(repository.getAllExpenses(projectName));
    }

    public void removeExpenses(String projectName, Map<Integer, UserExpense> selectedExpenses) {
        repository.removeExpenses(projectName, selectedExpenses.keySet());
        view.removeExpenses(selectedExpenses.keySet());
    }
}

package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;

import java.util.List;

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
        ExpenseProject project = repository.getProject(projectName);
        view.showExpenses(project.getExpenses());
    }

    public void showAddExpenseDialog(String projectName) {
        List<User> users = this.repository.getProject(projectName).getUsers();
        view.showAddExpenseDialog(users);
    }
}

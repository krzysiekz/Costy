package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;

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
}

package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;

import java.util.List;
import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.PeopleView;

public class PeoplePresenter implements Presenter<PeopleView> {

    private ProjectsRepository repository;
    private PeopleView view;

    public PeoplePresenter(ProjectsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(PeopleView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void loadProjectPeople(String projectName) {
        List<User> users = this.repository.getAllUsers(projectName);
        view.showPeople(users);
    }

    public void addPerson(String projectName, String personName) {
        User user = new User(personName);
        repository.addPerson(projectName, user);
        view.showPeople(repository.getAllUsers(projectName));
    }

    public void removeUsers(String projectName, Map<Integer, User> selectedUsers) {
        ExpenseProject project = this.repository.getProject(projectName);
        if (project.areUsersUsedInExpenses(selectedUsers.values())) {
            view.showPeopleUsedInExpensesError();
        } else {
            repository.removeUsers(projectName, selectedUsers.values());
            view.removePeople(selectedUsers.keySet());
        }
    }
}

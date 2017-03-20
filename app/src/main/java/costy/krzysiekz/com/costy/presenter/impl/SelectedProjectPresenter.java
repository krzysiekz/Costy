package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.ExpenseProject;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.SelectedProjectView;

public class SelectedProjectPresenter implements Presenter<SelectedProjectView> {

    private final ProjectsRepository repository;

    public SelectedProjectPresenter(ProjectsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(SelectedProjectView view) {

    }

    @Override
    public void detachView() {

    }

    public Boolean checkIfPeopleAdded(String projectName) {
        ExpenseProject project = repository.getProject(projectName);
        return project != null && !project.getUsers().isEmpty();
    }
}

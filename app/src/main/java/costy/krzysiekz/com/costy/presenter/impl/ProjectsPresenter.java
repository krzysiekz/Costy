package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import java.util.Map;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.ProjectsView;

public class ProjectsPresenter implements Presenter<ProjectsView> {

    private ProjectsView projectsView;
    private ProjectsRepository projectsRepository;

    public ProjectsPresenter(ProjectsRepository repository) {
        this.projectsRepository = repository;
    }

    @Override
    public void attachView(ProjectsView view) {
        this.projectsView = view;
    }

    @Override
    public void detachView() {
        this.projectsView = null;
    }

    public void addProject(String projectName, Currency currency) {
        projectsRepository.addProject(projectName, currency);
        projectsView.showProjects(projectsRepository.getAllProjects());
    }

    ProjectsView getProjectsView() {
        return projectsView;
    }

    public void removeProjects(Map<Integer, ExpenseProject> projects) {
        projectsRepository.removeProjects(projects.values());
        projectsView.removeProjects(projects.keySet());
    }

    public void loadProjects() {
        projectsView.showProjects(projectsRepository.getAllProjects());
    }
}

package costy.krzysiekz.com.costy.presenter.impl;


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

    public void addProject(String projectName) {
        projectsRepository.addProject(projectName);
        projectsView.showProjects(projectsRepository.getAllProjects());
    }

    ProjectsView getProjectsView() {
        return projectsView;
    }
}

package costy.krzysiekz.com.costy.model.dao.impl;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;

public class InMemoryProjectsRepository implements ProjectsRepository {

    private List<ExpenseProject> projects = new ArrayList<>();

    @Override
    public void addProject(String projectName) {
        projects.add(new ExpenseProject(projectName));
    }

    @Override
    public List<ExpenseProject> getAllProjects() {
        return projects;
    }

    @Override
    public ExpenseProject getProject(String s) {
        for (ExpenseProject project : projects) {
            if (project.getName().equals(s)) {
                return project;
            }
        }
        return null;
    }

    @Override
    public void updateProject(ExpenseProject project) {

    }
}

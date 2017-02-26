package costy.krzysiekz.com.costy.model.dao.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;

public class InMemoryProjectsRepository implements ProjectsRepository {

    private List<ExpenseProject> projects = new ArrayList<>();
    private List<Currency> supportedCurrencies = new ArrayList<>();

    public InMemoryProjectsRepository() {
        supportedCurrencies.add(new Currency("EUR"));
        supportedCurrencies.add(new Currency("PLN"));
    }

    @Override
    public void addProject(ExpenseProject project) {
        projects.add(project);
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

    @Override
    public void removeProjects(Collection<ExpenseProject> projects) {
        this.projects.removeAll(projects);
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return supportedCurrencies;
    }
}

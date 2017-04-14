package costy.krzysiekz.com.costy.model.dao.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

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

    @Override
    public List<User> getAllUsers(String projectName) {
        return getProject(projectName).getUsers();
    }

    @Override
    public List<UserExpense> getAllExpenses(String projectName) {
        return getProject(projectName).getExpenses();
    }

    @Override
    public void addPerson(String projectName, User user) {
        ExpenseProject project = getProject(projectName);
        project.addUser(user);
    }

    @Override
    public void addExpense(String projectName, UserExpense expense) {
        ExpenseProject project = getProject(projectName);
        project.addExpense(expense);
    }

    @Override
    public void removeExpenses(String projectName, Collection<Integer> positions) {
        ExpenseProject project = getProject(projectName);
        List<UserExpense> expenses = project.getExpenses();
        List<UserExpense> toRemove = StreamSupport.stream(positions).
                map(expenses::get).collect(Collectors.toList());
        project.removeExpenses(toRemove);
    }

    @Override
    public void removeUsers(String projectName, Collection<User> usersToRemove) {
        ExpenseProject project = getProject(projectName);
        project.removeUsers(usersToRemove);
    }

    @Override
    public Currency getProjectDefaultCurrency(String projectName) {
        ExpenseProject project = getProject(projectName);
        return project.getDefaultCurrency();
    }

    @Override
    public void changeDefaultCurrency(String projectName, String newCurrency) {
        ExpenseProject project = getProject(projectName);
        project.setDefaultCurrency(new Currency(newCurrency));
    }
}

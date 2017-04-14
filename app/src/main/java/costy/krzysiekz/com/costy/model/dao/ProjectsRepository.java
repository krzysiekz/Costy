package costy.krzysiekz.com.costy.model.dao;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.util.Collection;
import java.util.List;

public interface ProjectsRepository {
    void addProject(ExpenseProject project);

    List<ExpenseProject> getAllProjects();

    ExpenseProject getProject(String s);

    void updateProject(ExpenseProject project);

    void removeProjects(Collection<ExpenseProject> projects);

    List<Currency> getAllCurrencies();

    List<User> getAllUsers(String projectName);

    List<UserExpense> getAllExpenses(String projectName);

    void addPerson(String projectName, User user);

    void addExpense(String projectName, UserExpense expense);

    void removeExpenses(String projectName, Collection<Integer> positions);

    void removeUsers(String projectName, Collection<User> usersToRemove);

    Currency getProjectDefaultCurrency(String projectName);

    void changeDefaultCurrency(String projectName, String newCurrency);
}

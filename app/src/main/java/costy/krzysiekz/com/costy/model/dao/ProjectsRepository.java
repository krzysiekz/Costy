package costy.krzysiekz.com.costy.model.dao;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;

import java.util.Collection;
import java.util.List;

public interface ProjectsRepository {
    void addProject(String projectName, Currency currency);

    List<ExpenseProject> getAllProjects();

    ExpenseProject getProject(String s);

    void updateProject(ExpenseProject project);


    void removeProjects(Collection<ExpenseProject> projects);
}

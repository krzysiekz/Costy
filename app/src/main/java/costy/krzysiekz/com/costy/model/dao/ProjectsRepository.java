package costy.krzysiekz.com.costy.model.dao;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.List;

public interface ProjectsRepository {
    void addProject(String projectName);

    List<ExpenseProject> getAllProjects();

    ExpenseProject getProject(String s);
}

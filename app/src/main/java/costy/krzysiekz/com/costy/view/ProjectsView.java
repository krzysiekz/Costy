package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.ExpenseProject;

import java.util.List;
import java.util.Set;

public interface ProjectsView extends MVPView {
    void showProjects(List<ExpenseProject> expenseProjects);

    void removeProjects(Set<Integer> positions);
}

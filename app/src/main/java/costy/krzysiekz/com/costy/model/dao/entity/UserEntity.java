package costy.krzysiekz.com.costy.model.dao.entity;

import com.orm.SugarRecord;

public class UserEntity extends SugarRecord {
    private String name;


    private ExpenseProjectEntity expenseProject;

    public UserEntity(String name, ExpenseProjectEntity expenseProject) {
        this.name = name;
        this.expenseProject = expenseProject;
    }

    public UserEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpenseProjectEntity getExpenseProject() {
        return expenseProject;
    }

    public void setExpenseProject(ExpenseProjectEntity expenseProject) {
        this.expenseProject = expenseProject;
    }
}

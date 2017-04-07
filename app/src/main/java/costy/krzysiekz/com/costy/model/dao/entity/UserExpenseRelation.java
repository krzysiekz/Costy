package costy.krzysiekz.com.costy.model.dao.entity;

import com.orm.SugarRecord;

public class UserExpenseRelation extends SugarRecord {

    private UserEntity user;
    private UserExpenseEntity expense;

    public UserExpenseRelation() {
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserExpenseEntity getExpense() {
        return expense;
    }

    public void setExpense(UserExpenseEntity expense) {
        this.expense = expense;
    }
}

package costy.krzysiekz.com.costy.model.dao.entity;

import com.orm.SugarRecord;

public class UserExpenseEntity extends SugarRecord {

    private UserEntity user;
    private String amount;
    private String description;
    private CurrencyEntity currency;
    private ExpenseProjectEntity expenseProject;

    public UserExpenseEntity(UserEntity user, String amount, String description,
                             CurrencyEntity currency, ExpenseProjectEntity expenseProject) {
        this.user = user;
        this.amount = amount;
        this.description = description;
        this.currency = currency;
        this.expenseProject = expenseProject;
    }

    public UserExpenseEntity() {
    }

    public UserEntity getUser() {
        return user;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public CurrencyEntity getCurrency() {
        return currency;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrency(CurrencyEntity currency) {
        this.currency = currency;
    }

    public void setExpenseProject(ExpenseProjectEntity expenseProject) {
        this.expenseProject = expenseProject;
    }
}

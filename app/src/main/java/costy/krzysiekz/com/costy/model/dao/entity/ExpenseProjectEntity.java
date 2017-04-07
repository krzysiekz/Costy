package costy.krzysiekz.com.costy.model.dao.entity;

import com.orm.SugarRecord;

import java.util.List;

public class ExpenseProjectEntity extends SugarRecord {
    private String name;
    private CurrencyEntity defaultCurrency;

    public ExpenseProjectEntity() {
    }

    public ExpenseProjectEntity(String name, CurrencyEntity defaultCurrency) {
        this.name = name;
        this.defaultCurrency = defaultCurrency;
    }

    public String getName() {
        return name;
    }

    public CurrencyEntity getDefaultCurrency() {
        return defaultCurrency;
    }

    public List<UserEntity> getUsers() {
        return UserEntity.find(UserEntity.class, "EXPENSE_PROJECT = ?", String.valueOf(getId()));
    }

    public List<UserExpenseEntity> getExpenses() {
        return UserExpenseEntity.find(UserExpenseEntity.class, "EXPENSE_PROJECT = ?",
                new String[]{String.valueOf(getId())}, null, "ID", null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultCurrency(CurrencyEntity defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }
}

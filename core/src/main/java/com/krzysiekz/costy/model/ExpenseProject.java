package com.krzysiekz.costy.model;

import java.util.ArrayList;
import java.util.List;

public class ExpenseProject {
    private List<UserExpense> expenses;

    public ExpenseProject(String projectName) {
        this.expenses = new ArrayList<>();
    }

    public void addExpense(UserExpense userExpense) {
        expenses.add(userExpense);
    }

    public List<UserExpense> getExpenses() {
        return expenses;
    }
}

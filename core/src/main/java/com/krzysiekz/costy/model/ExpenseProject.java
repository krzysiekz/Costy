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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseProject that = (ExpenseProject) o;

        return expenses != null ? expenses.equals(that.expenses) : that.expenses == null;

    }

    @Override
    public int hashCode() {
        return expenses != null ? expenses.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExpenseProject{" +
                "expenses=" + expenses +
                '}';
    }
}

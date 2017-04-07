package com.krzysiekz.costy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExpenseProject {
    private List<UserExpense> expenses;
    private String name;
    private List<User> users;
    private Currency defaultCurrency;

    public ExpenseProject(String name) {
        this.expenses = new ArrayList<>();
        this.users = new ArrayList<>();
        this.name = name;
    }

    public ExpenseProject(String projectName, Currency defaultCurrency) {
        this.expenses = new ArrayList<>();
        this.name = projectName;
        this.users = new ArrayList<>();
        this.defaultCurrency = defaultCurrency;
    }

    public void addExpense(UserExpense userExpense) {
        expenses.add(userExpense);
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public List<UserExpense> getExpenses() {
        return expenses;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseProject project = (ExpenseProject) o;

        if (expenses != null ? !expenses.equals(project.expenses) : project.expenses != null)
            return false;
        if (name != null ? !name.equals(project.name) : project.name != null) return false;
        if (users != null ? !users.equals(project.users) : project.users != null) return false;
        return defaultCurrency != null ? defaultCurrency.equals(project.defaultCurrency) : project.defaultCurrency == null;

    }

    @Override
    public int hashCode() {
        int result = expenses != null ? expenses.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (defaultCurrency != null ? defaultCurrency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpenseProject{" +
                "expenses=" + expenses +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", defaultCurrency=" + defaultCurrency +
                '}';
    }

    public void removeExpenses(Collection<UserExpense> selectedExpenses) {
        this.expenses.removeAll(selectedExpenses);
    }

    public void removeUsers(Collection<User> users) {
        this.users.removeAll(users);
    }

    public boolean areUsersUsedInExpenses(Collection<User> users) {
        for (User user : users) {
            if (isUserUsedInExpenses(user)) return true;
        }
        return false;
    }

    private boolean isUserUsedInExpenses(User user) {
        for (UserExpense expense : expenses) {
            if (expense.isUserUsed(user)) {
                return true;
            }
        }
        return false;
    }
}

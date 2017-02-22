package com.krzysiekz.costy.model;

import java.math.BigDecimal;
import java.util.List;

public class UserExpense {

    private List<User> receivers;
    private User user;
    private BigDecimal amount;
    private String description;
    private Currency currency;

    private UserExpense(UserExpenseBuilder builder) {
        this.receivers = builder.receivers;
        this.user = builder.user;
        this.amount = builder.amount;
        this.description = builder.description;
        this.currency = builder.currency;
    }

    public List<User> getReceivers() {
        return receivers;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserExpense expense = (UserExpense) o;

        if (receivers != null ? !receivers.equals(expense.receivers) : expense.receivers != null)
            return false;
        if (user != null ? !user.equals(expense.user) : expense.user != null) return false;
        if (amount != null ? !amount.equals(expense.amount) : expense.amount != null) return false;
        if (description != null ? !description.equals(expense.description) : expense.description != null)
            return false;
        return currency != null ? currency.equals(expense.currency) : expense.currency == null;

    }

    @Override
    public int hashCode() {
        int result = receivers != null ? receivers.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserExpense{" +
                "receivers=" + receivers +
                ", user=" + user +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", currency=" + currency +
                '}';
    }

    boolean isUserUsed(User user) {
        return this.user.equals(user) || receivers.contains(user);
    }

    public static class UserExpenseBuilder {
        private List<User> receivers;
        private User user;
        private BigDecimal amount;
        private String description;
        private Currency currency;

        public UserExpenseBuilder() {
        }

        public UserExpenseBuilder withReceivers(List<User> receivers) {
            this.receivers = receivers;
            return this;
        }

        public UserExpenseBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public UserExpenseBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public UserExpenseBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public UserExpenseBuilder withCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public UserExpense build() {
            return new UserExpense(this);
        }
    }
}

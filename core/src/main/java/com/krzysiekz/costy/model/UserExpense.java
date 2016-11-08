package com.krzysiekz.costy.model;

import java.math.BigDecimal;

public class UserExpense {

    private User user;
    private BigDecimal amount;

    public UserExpense(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserExpense that = (UserExpense) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return amount != null ? amount.equals(that.amount) : that.amount == null;

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserExpense{" +
                "user=" + user +
                ", amount=" + amount +
                '}';
    }
}

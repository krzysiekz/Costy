package com.krzysiekz.costy.model;

import java.math.BigDecimal;
import java.util.List;

public class UserExpense {

    private List<User> receivers;
    private User user;
    private BigDecimal amount;
    private String description;

    public UserExpense(User user, BigDecimal amount, List<User> receivers, String description) {
        this.user = user;
        this.amount = amount;
        this.receivers = receivers;
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public List<User> getReceivers() {
        return receivers;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserExpense that = (UserExpense) o;

        if (receivers != null ? !receivers.equals(that.receivers) : that.receivers != null)
            return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;

    }

    @Override
    public int hashCode() {
        int result = receivers != null ? receivers.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserExpense{" +
                "receivers=" + receivers +
                ", user=" + user +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    boolean isUserUsed(User user) {
        return user.equals(user) || receivers.contains(user);
    }
}

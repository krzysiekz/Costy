package com.krzysiekz.costy.model;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Locale;

public class ReportEntry {
    private User sender;
    private User receiver;
    private BigDecimal amount;
    private Currency currency;

    private ReportEntry(Builder builder) {
        sender = builder.sender;
        receiver = builder.receiver;
        setAmount(builder.amount);
        currency = builder.currency;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportEntry entry = (ReportEntry) o;

        if (sender != null ? !sender.equals(entry.sender) : entry.sender != null) return false;
        if (receiver != null ? !receiver.equals(entry.receiver) : entry.receiver != null)
            return false;
        if (amount != null ? !amount.equals(entry.amount) : entry.amount != null) return false;
        return currency != null ? currency.equals(entry.currency) : entry.currency == null;

    }

    @Override
    public int hashCode() {
        int result = sender != null ? sender.hashCode() : 0;
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new MessageFormat("{0} -> {1}: {2} {3}", Locale.US).
                format(new Object[]{sender, receiver, amount, currency});
    }

    public static final class Builder {
        private User sender;
        private User receiver;
        private BigDecimal amount;
        private Currency currency;

        public Builder() {
        }

        public Builder withSender(User val) {
            sender = val;
            return this;
        }

        public Builder withReceiver(User val) {
            receiver = val;
            return this;
        }

        public Builder withAmount(BigDecimal val) {
            amount = val;
            return this;
        }

        public Builder withCurrency(Currency val) {
            currency = val;
            return this;
        }

        public ReportEntry build() {
            return new ReportEntry(this);
        }
    }
}

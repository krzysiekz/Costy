package com.krzysiekz.costy.model;

public class Currency {

    private String name;

    public Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return name != null ? name.equals(currency.name) : currency.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                '}';
    }
}

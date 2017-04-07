package costy.krzysiekz.com.costy.model.dao.entity;

import com.orm.SugarRecord;

public class CurrencyEntity extends SugarRecord {

    private String name;

    public CurrencyEntity() {
    }

    public CurrencyEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

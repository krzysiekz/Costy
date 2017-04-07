package costy.krzysiekz.com.costy.model.dao.converter.impl;

import com.krzysiekz.costy.model.Currency;

import costy.krzysiekz.com.costy.model.dao.converter.Converter;
import costy.krzysiekz.com.costy.model.dao.entity.CurrencyEntity;

public class CurrencyConverter implements Converter<CurrencyEntity, Currency> {

    @Override
    public CurrencyEntity toEntity(Currency businessObject) {
        return new CurrencyEntity(businessObject.getName());
    }

    @Override
    public Currency fromEntity(CurrencyEntity entity) {
        return new Currency(entity.getName());
    }
}

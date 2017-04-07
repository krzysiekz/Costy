package costy.krzysiekz.com.costy.model.dao.converter.impl;

import com.krzysiekz.costy.model.ExpenseProject;

import costy.krzysiekz.com.costy.model.dao.converter.Converter;
import costy.krzysiekz.com.costy.model.dao.entity.ExpenseProjectEntity;

public class ExpenseProjectConverter implements Converter<ExpenseProjectEntity, ExpenseProject> {

    @Override
    public ExpenseProjectEntity toEntity(ExpenseProject businessObject) {
        ExpenseProjectEntity entity = new ExpenseProjectEntity();
        entity.setName(businessObject.getName());
        return entity;
    }

    @Override
    public ExpenseProject fromEntity(ExpenseProjectEntity entity) {
        return new ExpenseProject(entity.getName());
    }
}

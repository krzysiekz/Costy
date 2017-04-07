package costy.krzysiekz.com.costy.model.dao.converter;


import com.orm.SugarRecord;

public interface Converter<E extends SugarRecord, B> {
    E toEntity(B businessObject);

    B fromEntity(E entity);
}

package costy.krzysiekz.com.costy;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

public class IntegrationTestCostyApplication extends CostyApplication {

    protected void prepareDBIfNeeded() {
        recreateSqlTables();
        super.prepareDBIfNeeded();
    }

    private void recreateSqlTables() {
        SugarContext.terminate();
        SchemaGenerator schemaGenerator = new SchemaGenerator(getApplicationContext());
        schemaGenerator.deleteTables(new SugarDb(getApplicationContext()).getDB());
        SugarContext.init(getApplicationContext());
        schemaGenerator.createDatabase(new SugarDb(getApplicationContext()).getDB());
    }

}

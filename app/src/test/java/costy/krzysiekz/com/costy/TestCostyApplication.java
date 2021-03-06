package costy.krzysiekz.com.costy;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import costy.krzysiekz.com.costy.model.di.PresenterModule;

public class TestCostyApplication extends CostyApplication {

    private PresenterModule presenterModule;

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

    @Override
    protected PresenterModule getPresenterModule() {
        if (presenterModule == null) {
            presenterModule = super.getPresenterModule();
        }
        return presenterModule;
    }

    public void setPresenterModule(PresenterModule presenterModule) {
        this.presenterModule = presenterModule;
        initComponent();
    }
}

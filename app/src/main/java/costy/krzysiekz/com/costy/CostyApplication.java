package costy.krzysiekz.com.costy;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.orm.SugarApp;
import com.orm.SugarDb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import costy.krzysiekz.com.costy.model.dao.entity.CurrencyEntity;
import costy.krzysiekz.com.costy.model.di.DaggerGraphComponent;
import costy.krzysiekz.com.costy.model.di.GraphComponent;
import costy.krzysiekz.com.costy.model.di.MainModule;
import costy.krzysiekz.com.costy.model.di.PresenterModule;

public class CostyApplication extends SugarApp {

    protected static final String SUGAR_UPGRADES_INIT_SQL = "sugar_upgrades/init.sql";
    private static final String TAG = "Sugar";

    private static GraphComponent graph;

    @Override
    public void onCreate() {
        super.onCreate();
        prepareDBIfNeeded();
    }

    protected void prepareDBIfNeeded() {
        SQLiteDatabase sqLiteDatabase = new SugarDb(getApplicationContext()).getDB();
        if (sqLiteDatabase.getVersion() == 1 && CurrencyEntity.listAll(CurrencyEntity.class).isEmpty()) {
            executeScript(sqLiteDatabase, SUGAR_UPGRADES_INIT_SQL);
        }
        initComponent();
    }

    protected void executeScript(SQLiteDatabase db, String file) {
        try {
            InputStream is = this.getApplicationContext().getAssets().open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i(TAG, line);
                db.execSQL(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    protected void initComponent() {
        graph = DaggerGraphComponent.builder()
                .mainModule(getMainModule())
                .presenterModule(getPresenterModule())
                .build();
    }

    protected PresenterModule getPresenterModule() {
        return new PresenterModule();
    }

    protected MainModule getMainModule() {
        return new MainModule(this);
    }

    public static GraphComponent component() {
        return graph;
    }
}

package costy.krzysiekz.com.costy;


import android.app.Application;

import costy.krzysiekz.com.costy.model.di.DaggerGraphComponent;
import costy.krzysiekz.com.costy.model.di.GraphComponent;
import costy.krzysiekz.com.costy.model.di.MainModule;
import costy.krzysiekz.com.costy.model.di.PresenterModule;

public class CostyApplication extends Application {

    private static GraphComponent graph;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
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

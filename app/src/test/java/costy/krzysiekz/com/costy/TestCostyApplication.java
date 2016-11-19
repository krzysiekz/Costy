package costy.krzysiekz.com.costy;

import costy.krzysiekz.com.costy.model.di.PresenterModule;

public class TestCostyApplication extends CostyApplication {

    private PresenterModule presenterModule;

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

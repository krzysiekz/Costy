package costy.krzysiekz.com.costy.presenter;

import costy.krzysiekz.com.costy.view.MVPView;

public interface Presenter<V extends MVPView> {

    void attachView(V view);

    void detachView();
}

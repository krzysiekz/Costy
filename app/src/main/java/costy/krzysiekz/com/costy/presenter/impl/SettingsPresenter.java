package costy.krzysiekz.com.costy.presenter.impl;

import com.krzysiekz.costy.model.Currency;

import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.presenter.Presenter;
import costy.krzysiekz.com.costy.view.SettingsView;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class SettingsPresenter implements Presenter<SettingsView> {

    private ProjectsRepository repository;
    private SettingsView view;

    public SettingsPresenter(ProjectsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(SettingsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    public void setUpCurrencies(String projectName) {
        Currency defaultCurrency = repository.getProjectDefaultCurrency(projectName);
        List<Currency> allCurrencies = repository.getAllCurrencies();
        view.setUpCurrencies(defaultCurrency.getName(),
                StreamSupport.stream(allCurrencies).map(Currency::getName).collect(Collectors.toList()));
    }

    public void changeDefaultCurrency(String projectName, String newDefaultCurrency) {
        Currency defaultCurrency = repository.getProjectDefaultCurrency(projectName);
        if (!defaultCurrency.getName().equals(newDefaultCurrency)) {
            repository.changeDefaultCurrency(projectName, newDefaultCurrency);
            view.showDefaultCurrencyChangedText();
        }
    }
}

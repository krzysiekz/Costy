package costy.krzysiekz.com.costy.presenter.impl;


import com.krzysiekz.costy.model.Currency;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.view.SettingsView;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsPresenterTest {

    private SettingsView view;
    private ProjectsRepository repository;
    private SettingsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        view = mock(SettingsView.class);
        repository = mock(ProjectsRepository.class);
        presenter = new SettingsPresenter(repository);
    }

    @Test
    public void shouldCallViewWithProperValuesWhenSettingUpCurrencies() {
        //given
        String projectName = "Some project name";
        Currency currency = new Currency("PLN");
        Currency secondCurrency = new Currency("USD");
        //when
        when(repository.getProjectDefaultCurrency(projectName)).thenReturn(currency);
        when(repository.getAllCurrencies()).thenReturn(Arrays.asList(currency, secondCurrency));
        presenter.attachView(view);
        presenter.setUpCurrencies(projectName);
        //then
        verify(view).setUpCurrencies("PLN", Arrays.asList("PLN", "USD"));
    }

    @Test
    public void shouldChangeDefaultCurrency() {
        //given
        String projectName = "Some project name";
        Currency currency = new Currency("PLN");
        //when
        when(repository.getProjectDefaultCurrency(projectName)).thenReturn(currency);
        presenter.attachView(view);
        presenter.changeDefaultCurrency(projectName, "USD");
        //then
        verify(repository).changeDefaultCurrency(projectName, "USD");
        verify(view).showDefaultCurrencyChangedText();
    }

    @Test
    public void shouldNotChangeDefaultCurrencyIfAlreadySet() {
        //given
        String projectName = "Some project name";
        Currency currency = new Currency("PLN");
        //when
        when(repository.getProjectDefaultCurrency(projectName)).thenReturn(currency);
        presenter.attachView(view);
        presenter.changeDefaultCurrency(projectName, "PLN");
        //then
        verify(repository, never()).changeDefaultCurrency(anyString(), anyString());
        verify(view, never()).showDefaultCurrencyChangedText();
    }
}
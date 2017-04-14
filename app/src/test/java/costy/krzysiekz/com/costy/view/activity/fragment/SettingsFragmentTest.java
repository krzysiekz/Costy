package costy.krzysiekz.com.costy.view.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.SpinnerAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Arrays;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.SettingsPresenter;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24, application = TestCostyApplication.class)
public class SettingsFragmentTest {

    private PresenterModuleMock presenterModuleMock;
    private SettingsFragment fragment;
    private static final String PROJECT_NAME = "Some project name";

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        fragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SelectedProjectActivity.PROJECT_NAME, PROJECT_NAME);
        fragment.setArguments(bundle);
        //when
        SupportFragmentTestUtil.startVisibleFragment(fragment,
                SelectedProjectActivity.class, R.id.selected_project_content);
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(fragment.presenter).isNotNull().isInstanceOf(SettingsPresenter.class);
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getSettingsPresenter()).attachView(fragment);
    }

    @Test
    public void shouldSetUpDefaultCurrencySpinner() {
        //then
        verify(presenterModuleMock.getSettingsPresenter()).setUpCurrencies(PROJECT_NAME);
    }

    @Test
    public void defaultCurrencyShouldBeSelectedByDefault() {
        //when
        fragment.setUpCurrencies("EUR", Arrays.asList("EUR", "PLN", "USD"));
        String selected = (String) fragment.defaultCurrencySpinner.getSelectedItem();
        //then
        assertThat(selected).isEqualTo("EUR");
    }

    @Test
    public void shouldShowAllAvailableCurrenciesInSpinner() {
        //when
        fragment.setUpCurrencies("EUR", Arrays.asList("EUR", "PLN", "USD"));
        //when
        SpinnerAdapter currencySpinnerAdapter = fragment.defaultCurrencySpinner.getAdapter();
        //then
        assertThat(currencySpinnerAdapter.getCount()).isEqualTo(3);
        assertThat(currencySpinnerAdapter.getItem(0)).isEqualTo("EUR");
        assertThat(currencySpinnerAdapter.getItem(1)).isEqualTo("PLN");
        assertThat(currencySpinnerAdapter.getItem(2)).isEqualTo("USD");
    }

    @Test
    public void shouldCallPresenterWhenDefaultCurrencyChanged() {
        //when
        fragment.setUpCurrencies("EUR", Arrays.asList("EUR", "PLN", "USD"));
        fragment.defaultCurrencySpinner.setSelection(1);
        //then
        verify(presenterModuleMock.getSettingsPresenter(), times(2)).changeDefaultCurrency(PROJECT_NAME, "EUR");
        verify(presenterModuleMock.getSettingsPresenter(), times(2)).changeDefaultCurrency(PROJECT_NAME, "PLN");
    }

    @Test
    public void shouldShowToastWhenDefaultCurrencyWasChanged() {
        //given
        final Context context = RuntimeEnvironment.application;
        //when
        fragment.showDefaultCurrencyChangedText();
        //then
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).
                isEqualTo(context.getString(R.string.default_currency_changed));
    }
}
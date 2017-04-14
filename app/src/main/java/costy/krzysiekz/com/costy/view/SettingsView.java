package costy.krzysiekz.com.costy.view;

import java.util.List;

public interface SettingsView extends MVPView {
    void setUpCurrencies(String defaultCurrency, List<String> availableCurrencies);

    void showDefaultCurrencyChangedText();
}

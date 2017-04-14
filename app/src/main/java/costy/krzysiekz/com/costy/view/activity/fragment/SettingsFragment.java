package costy.krzysiekz.com.costy.view.activity.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import costy.krzysiekz.com.costy.CostyApplication;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.presenter.impl.SettingsPresenter;
import costy.krzysiekz.com.costy.view.SettingsView;

import static costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity.PROJECT_NAME;

public class SettingsFragment extends Fragment implements SettingsView {

    private String projectName;
    SettingsPresenter presenter;

    @BindView(R.id.settings_default_currency)
    Spinner defaultCurrencySpinner;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        CostyApplication.component().inject(this);

        this.projectName = getArguments().getString(PROJECT_NAME);
        presenter.attachView(this);
        presenter.setUpCurrencies(projectName);

        return view;
    }

    @Inject
    void setPresenter(SettingsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setUpCurrencies(String defaultCurrency, List<String> availableCurrencies) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, availableCurrencies);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        defaultCurrencySpinner.setAdapter(dataAdapter);
        defaultCurrencySpinner.setSelection(availableCurrencies.indexOf(defaultCurrency));
    }

    @Override
    public void showDefaultCurrencyChangedText() {
        Toast.makeText(getContext(), R.string.default_currency_changed, Toast.LENGTH_SHORT).show();
    }

    @OnItemSelected(R.id.settings_default_currency)
    public void spinnerItemSelected(Spinner spinner, int position) {
        presenter.changeDefaultCurrency(projectName, spinner.getAdapter().getItem(position).toString());
    }
}

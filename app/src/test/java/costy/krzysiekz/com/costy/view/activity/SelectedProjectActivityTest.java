package costy.krzysiekz.com.costy.view.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.SelectedProjectPresenter;
import costy.krzysiekz.com.costy.view.activity.fragment.ExpensesFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.SettingsFragment;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestCostyApplication.class)
public class SelectedProjectActivityTest {

    private static final String PROJECT_NAME = "Some project name";

    private SelectedProjectActivity selectedProjectActivity;
    private PresenterModuleMock presenterModuleMock;

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        initActivity();
    }

    private void initActivity() {
        Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(), SelectedProjectActivity.class);
        intent.putExtra(SelectedProjectActivity.PROJECT_NAME, PROJECT_NAME);
        selectedProjectActivity = Robolectric.buildActivity(SelectedProjectActivity.class).withIntent(intent).
                create().start().resume().get();
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldShowExpensesFragment() {
        //given
        RoboMenuItem item = new RoboMenuItem(R.id.nav_expenses);
        //when
        selectedProjectActivity.onNavigationItemSelected(item);
        //then
        List<Fragment> fragments = selectedProjectActivity.getSupportFragmentManager().getFragments();
        fragments = StreamSupport.stream(fragments).filter(e -> e != null).collect(Collectors.toList());
        assertThat(fragments).isNotEmpty().hasSize(1);
        assertThat(fragments.get(0)).isInstanceOf(ExpensesFragment.class);
    }

    @Test
    public void shouldShowSettingsFragment() {
        //given
        RoboMenuItem item = new RoboMenuItem(R.id.nav_settings);
        //when
        selectedProjectActivity.onNavigationItemSelected(item);
        //then
        List<Fragment> fragments = selectedProjectActivity.getSupportFragmentManager().getFragments();
        fragments = StreamSupport.stream(fragments).filter(e -> e != null).collect(Collectors.toList());
        assertThat(fragments).isNotEmpty().hasSize(1);
        assertThat(fragments.get(0)).isInstanceOf(SettingsFragment.class);
    }

    @Test
    public void shouldShowPeopleFragment() {
        //given
        RoboMenuItem item = new RoboMenuItem(R.id.nav_settings);
        RoboMenuItem peopleItem = new RoboMenuItem(R.id.nav_people);
        //when
        selectedProjectActivity.onNavigationItemSelected(item);
        selectedProjectActivity.onNavigationItemSelected(peopleItem);
        //then
        List<Fragment> fragments = selectedProjectActivity.getSupportFragmentManager().getFragments();
        fragments = StreamSupport.stream(fragments).filter(e -> e != null).collect(Collectors.toList());
        assertThat(fragments).isNotEmpty().hasSize(1);
        assertThat(fragments.get(0)).isInstanceOf(PeopleFragment.class);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(selectedProjectActivity.presenter).isNotNull().
                isInstanceOf(SelectedProjectPresenter.class);
    }

    @Test
    public void shouldShowPeopleFragmentIfNoPeopleInProject() {
        //given
        SelectedProjectPresenter presenter = presenterModuleMock.getSelectedProjectPresenter();
        //then
        verify(presenter).checkIfPeopleAdded(PROJECT_NAME);
        List<Fragment> fragments = selectedProjectActivity.getSupportFragmentManager().getFragments();
        assertThat(fragments).isNotEmpty().hasSize(1);
        assertThat(fragments.get(0)).isInstanceOf(PeopleFragment.class);
    }

    @Test
    public void shouldShowExpensesFragmentIfPeopleAddedToProject() throws Exception {
        //given
        SelectedProjectPresenter presenter = mock(SelectedProjectPresenter.class);
        //when
        when(presenter.checkIfPeopleAdded(PROJECT_NAME)).thenReturn(true);
        setUpModulesMocks();
        presenterModuleMock.setSelectedProjectPresenter(presenter);
        initActivity();
        //then
        verify(presenter).checkIfPeopleAdded(PROJECT_NAME);
        List<Fragment> fragments = selectedProjectActivity.getSupportFragmentManager().getFragments();
        assertThat(fragments).isNotEmpty().hasSize(1);
        assertThat(fragments.get(0)).isInstanceOf(ExpensesFragment.class);
    }
}
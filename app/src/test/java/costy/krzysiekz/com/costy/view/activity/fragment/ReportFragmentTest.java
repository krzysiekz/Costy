package costy.krzysiekz.com.costy.view.activity.fragment;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24, application = TestCostyApplication.class)
public class ReportFragmentTest {

    private PresenterModuleMock presenterModuleMock;
    private ReportFragment fragment;
    private static final String PROJECT_NAME = "Some project name";

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        fragment = new ReportFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SelectedProjectActivity.PROJECT_NAME, PROJECT_NAME);
        fragment.setArguments(bundle);
        //when
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(fragment.presenter).isNotNull().isInstanceOf(ReportPresenter.class);
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getReportPresenter()).attachView(fragment);
    }

    @Test
    public void shouldLoadReportEntriesUponActivityStart() {
        //then
        verify(presenterModuleMock.getReportPresenter()).loadReportEntries(PROJECT_NAME);
    }

}
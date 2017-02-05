package costy.krzysiekz.com.costy.view.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.ReportEntry;
import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.math.BigDecimal;
import java.util.Collections;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.ReportPresenter;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.adapter.ReportEntryAdapter;

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

    @Test
    public void shouldPassReportEntriesToAdapter() {
        //given
        User kate = new User("Kate");
        User john = new User("John");
        ReportEntry entry = new ReportEntry(kate, john, new BigDecimal("10"));
        //when
        RecyclerView recyclerView = fragment.reportEntriesRecyclerView;
        fragment.showReportEntries(Collections.singletonList(entry));
        //then
        assertThat(recyclerView).isNotNull();
        assertThat(recyclerView.getAdapter()).isInstanceOf(ReportEntryAdapter.class);
        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(1);
        assertThat(((ReportEntryAdapter) recyclerView.getAdapter()).getReportEntries()).containsOnly(entry);
    }

}
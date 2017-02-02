package costy.krzysiekz.com.costy.view.activity.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.math.BigDecimal;
import java.util.Arrays;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestCostyApplication.class)
public class ExpensesFragmentTest {

    private PresenterModuleMock presenterModuleMock;
    private ExpensesFragment fragment;
    private static final String PROJECT_NAME = "Some project name";

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        fragment = new ExpensesFragment();
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
        assertThat(fragment.presenter).isNotNull().isInstanceOf(ExpensesPresenter.class);
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getExpensesPresenter()).attachView(fragment);
    }

    @Test
    public void shouldPassExpensesToAdapter() {
        //given
        User kate = new User("Kate");
        User john = new User("John");
        UserExpense firstExpense = new UserExpense(kate, new BigDecimal("10.50"), Arrays.asList(kate, john), "");
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Arrays.asList(kate, john), "");
        //when
        RecyclerView recyclerView = fragment.expensesRecyclerView;
        fragment.showExpenses(Arrays.asList(firstExpense, secondExpense));
        //then
        assertThat(recyclerView).isNotNull();
        assertThat(recyclerView.getAdapter()).isInstanceOf(ExpensesAdapter.class);
        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(2);
        assertThat(((ExpensesAdapter) recyclerView.getAdapter()).getItems()).containsOnly(firstExpense, secondExpense);
    }

    @Test
    public void shouldImplementProjectsView() {
        //then
        assertThat(fragment).isInstanceOf(ExpensesView.class);
    }

    @Test
    public void shouldLoadProjectExpensesUponFragmentStart() {
        //then
        verify(presenterModuleMock.getExpensesPresenter()).loadProjectExpenses(PROJECT_NAME);
    }

    @Test
    public void shouldCallDialogFragmentAfterClickingAddExpenseButton() {
        //when
        fragment.addExpenseButton.performClick();
        //then
        verify(presenterModuleMock.getExpensesPresenter()).showAddExpenseDialog(PROJECT_NAME);
    }


}
package costy.krzysiekz.com.costy.view.activity;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.math.BigDecimal;
import java.util.Arrays;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;
import costy.krzysiekz.com.costy.view.activity.adapter.ProjectAdapter;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23,
        application = TestCostyApplication.class)
public class ExpensesActivityTest {

    private static final String PROJECT_NAME = "Some project name";

    private ExpensesActivity expensesActivity;
    private PresenterModuleMock presenterModuleMock;

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(), ExpensesActivity.class);
        intent.putExtra(ExpensesActivity.PROJECT_NAME, PROJECT_NAME);
        expensesActivity = Robolectric.buildActivity(ExpensesActivity.class).withIntent(intent).
                create().start().resume().get();

    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldImplementProjectsView() {
        //then
        assertThat(expensesActivity).isInstanceOf(ExpensesView.class);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(expensesActivity.presenter).isNotNull();
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getExpensesPresenter()).attachView(expensesActivity);
    }

    @Test
    public void shouldLoadProjectUponActivityStart() {
        //then
        verify(presenterModuleMock.getExpensesPresenter()).loadProjectExpenses(PROJECT_NAME);
    }

    @Test
    public void shouldPassExpensesToAdapter() {
        //given
        User kate = new User("Kate");
        User john = new User("John");
        UserExpense firstExpense = new UserExpense(kate, new BigDecimal("10.50"), Arrays.asList(kate, john));
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Arrays.asList(kate, john));
        //when
        RecyclerView recyclerView = expensesActivity.expensesRecyclerView;
        expensesActivity.showExpenses(Arrays.asList(firstExpense, secondExpense));
        //then
        assertThat(recyclerView).isNotNull();
        assertThat(recyclerView.getAdapter()).isInstanceOf(ExpensesAdapter.class);
        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(2);
        assertThat(((ExpensesAdapter) recyclerView.getAdapter()).getItems()).containsOnly(firstExpense, secondExpense);
    }
}
package costy.krzysiekz.com.costy.view.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.ExpensesPresenter;
import costy.krzysiekz.com.costy.view.ExpensesView;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.adapter.ExpensesAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddExpenseDialogFragment;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24, application = TestCostyApplication.class)
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

    @Test
    public void shouldCallPresenterWhenExpenseConfirmed() {
        //given
        User john = new User("John");
        User kate = new User("Kate");
        UserExpense expense = new UserExpense(john, new BigDecimal("10"),
                Arrays.asList(john, kate), "Sample expense");
        //when
        fragment.onExpenseConfirmed(expense);
        //then
        verify(presenterModuleMock.getExpensesPresenter()).addExpense(PROJECT_NAME, expense);
    }

    @Test
    public void longClickShouldStartSelectionModeAndSelectItem() {
        //given
        RecyclerView recyclerView = fragment.expensesRecyclerView;
        ExpensesAdapter adapter = (ExpensesAdapter) recyclerView.getAdapter();
        User kate = new User("Kate");
        User john = new User("John");
        UserExpense firstExpense = new UserExpense(kate, new BigDecimal("10.50"), Arrays.asList(kate, john), "");
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Arrays.asList(kate, john), "");
        //when
        fragment.showExpenses(Arrays.asList(firstExpense, secondExpense));
        fragment.onItemLongClicked(0);
        //then
        assertThat(fragment.actionMode).isNotNull();
        assertThat(adapter.getSelectedItemCount()).isEqualTo(1);
        assertThat(adapter.getSelectedItems()).isNotEmpty().containsOnly(0);
    }

    @Test
    public void shortClickShouldSelectItemInSelectionMode() {
        //given
        RecyclerView recyclerView = fragment.expensesRecyclerView;
        ExpensesAdapter adapter = (ExpensesAdapter) recyclerView.getAdapter();
        User kate = new User("Kate");
        User john = new User("John");
        UserExpense firstExpense = new UserExpense(kate, new BigDecimal("10.50"), Arrays.asList(kate, john), "");
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Arrays.asList(kate, john), "");
        //when
        fragment.showExpenses(Arrays.asList(firstExpense, secondExpense));
        fragment.onItemLongClicked(0);
        fragment.onItemClicked(1);
        //then
        assertThat(adapter.getSelectedItemCount()).isEqualTo(2);
        assertThat(adapter.getSelectedItems()).isNotEmpty().containsOnly(0, 1);
    }

    @Test
    public void shouldShowDeleteButtonWhenInSelectionMode() {
        //given
        User john = new User("John");
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Collections.singletonList(john), "");
        //when
        fragment.showExpenses(Collections.singletonList(secondExpense));
        fragment.onItemLongClicked(0);
        MenuItem deleteItem = fragment.actionMode.getMenu().findItem(R.id.menu_remove);
        //then
        assertThat(deleteItem).isNotNull();
    }

    @Test
    public void shouldCallPresenterWhileRemovingItems() {
        //given
        User john = new User("John");
        UserExpense expense = new UserExpense(john, new BigDecimal("13"), Collections.singletonList(john), "");
        Map<Integer, UserExpense> expectedArgument = new HashMap<>();
        expectedArgument.put(0, expense);
        //when
        fragment.showExpenses(Collections.singletonList(expense));
        fragment.onItemLongClicked(0);
        MenuItem deleteItem = fragment.actionMode.getMenu().findItem(R.id.menu_remove);
        fragment.onActionItemClicked(fragment.actionMode, deleteItem);
        //then
        verify(presenterModuleMock.getExpensesPresenter()).removeExpenses(PROJECT_NAME, expectedArgument);
    }

    @Test
    public void shouldRemoveExpensesFromView() {
        //given
        int itemPosition = 0;
        RecyclerView recyclerView = fragment.expensesRecyclerView;
        ExpensesAdapter adapter = (ExpensesAdapter) recyclerView.getAdapter();
        Set<Integer> positions = new HashSet<>();
        positions.add(itemPosition);
        User john = new User("John");
        UserExpense secondExpense = new UserExpense(john, new BigDecimal("13"), Collections.singletonList(john), "");
        //when
        fragment.showExpenses(Collections.singletonList(secondExpense));
        fragment.onItemLongClicked(itemPosition);
        fragment.removeExpenses(positions);
        //then
        assertThat(adapter.getSelectedItemCount()).isEqualTo(0);
        assertThat(adapter.getSelectedItems()).isEmpty();
        assertThat(adapter.getItemCount()).isEqualTo(0);
        assertThat(adapter.getItems()).isEmpty();
    }

    @Test
    public void shouldShowAddExpenseDialog() {
        //given
        User john = new User("John");
        //when
        fragment.showAddExpenseDialog(Collections.singletonList(john));
        Fragment dialog = fragment.getActivity().getSupportFragmentManager().
                findFragmentByTag(AddExpenseDialogFragment.TAG);
        //then
        assertThat(dialog).isNotNull();
        assertThat(dialog).isInstanceOf(AddExpenseDialogFragment.class);
    }
}
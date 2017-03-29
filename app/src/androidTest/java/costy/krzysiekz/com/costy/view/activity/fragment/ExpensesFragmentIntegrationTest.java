package costy.krzysiekz.com.costy.view.activity.fragment;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.utils.ElapsedTimeIdlingResource;
import costy.krzysiekz.com.costy.utils.IntegrationTestUtils;
import costy.krzysiekz.com.costy.utils.RecyclerViewItemCountAssertion;
import costy.krzysiekz.com.costy.utils.ToastMatcher;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;
import java8.util.stream.StreamSupport;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.addProjectAndClickOnIt;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.clickNavigationDrawerItem;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.createExpense;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.rotateScreen;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExpensesFragmentIntegrationTest {

    private static final List<String> USERS = Arrays.asList("User 1", "User 2");
    private static final String PROJECT_TEST_NAMING = "Expenses Fragment Test {0}";
    private static final String DEFAULT_CURRENCY = "PLN";

    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    private static AtomicInteger testCounter = new AtomicInteger(0);
    private IdlingResource idlingResource;


    @Before
    public void setUp() throws Exception {
        addProjectAndClickOnIt(MessageFormat.format(PROJECT_TEST_NAMING,
                testCounter.incrementAndGet()), DEFAULT_CURRENCY);
        StreamSupport.stream(USERS).forEach(IntegrationTestUtils::createUser);
        clickNavigationDrawerItem(R.id.nav_expenses);
        //wait a bit before interacting with fragment since there is a timing issue here
        idlingResource = new ElapsedTimeIdlingResource(200);
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void tearDown() throws Exception {
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void shouldPopulatePayerSpinnerWithUsers() {
        //when
        onView(withId(R.id.add_expense_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.add_expense_dialog_from)).check(matches(isDisplayed())).perform(click());
        //then
        StreamSupport.stream(USERS).forEach(u -> onData(allOf(is(instanceOf(String.class)), is(u))).
                inRoot(isPlatformPopup()).check(matches(isDisplayed())));
    }

    @Test
    public void shouldPopulateReceiversSpinnerWithUsers() {
        //when
        onView(withId(R.id.add_expense_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.add_expense_receivers)).check(matches(isDisplayed())).perform(click());
        //then
        StreamSupport.stream(USERS).forEach(u -> onData(allOf(is(instanceOf(String.class)), is(u))).
                check(matches(isDisplayed())));
    }

    @Test
    public void shouldShowToastWhenNoReceiversSelected() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, USERS, DEFAULT_CURRENCY);
        //then
        onView(withText(R.string.add_expense_select_receivers_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldNotAcceptLettersAsAmount() {
        //given
        String amount = "abc";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        //then
        onView(withText(R.string.add_expense_provide_amount_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowToastWhenAmountNotProvided() {
        //given
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), "", description, Collections.emptyList(), DEFAULT_CURRENCY);
        //then
        onView(withText(R.string.add_expense_provide_amount_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowToastWhenDescriptionNotProvided() {
        //given
        String amount = "10";
        //when
        createExpense(USERS.get(0), amount, "", Collections.emptyList(), DEFAULT_CURRENCY);
        //then
        onView(withText(R.string.add_expense_provide_description_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void shouldAddExpense() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        //then
        onView(withId(R.id.expenses_recycler_view)).
                check(matches(hasDescendant(withId(R.id.item_expense_description))));
        onView(withId(R.id.expenses_recycler_view)).
                check(matches(hasDescendant(withText(description))));
    }

    @Test
    public void shouldHaveActionModeDisabledByDefault() {
        //then
        onView(withId(R.id.menu_remove)).check(doesNotExist());
    }

    @Test
    public void shouldHaveActionModeEnabledWhenItemLongClicked() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        onView(withId(R.id.expenses_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(description)), longClick()));
        //then
        onView(withId(R.id.menu_remove)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldRemoveExpenseFromProject() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        onView(withId(R.id.expenses_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(description)), longClick()));
        onView(withId(R.id.menu_remove)).perform(click());
        //then
        onView(withId(R.id.expenses_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void shouldHaveDefaultCurrencySelected() {
        //when
        onView(withId(R.id.add_expense_button)).check(matches(isDisplayed())).perform(click());
        //then
        onView(withId(R.id.add_expense_currency)).
                check(matches(withSpinnerText(containsString(DEFAULT_CURRENCY))));
    }

    @Test
    public void shouldKeepValuesInPopupWhenScreenRotated() {
        //given
        String amount = "10";
        String description = "Sample description";
        String currency = "EUR";
        //when
        onView(withId(R.id.add_expense_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.add_expense_dialog_from)).check(matches(isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(USERS.get(0)))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.add_expense_amount)).check(matches(isDisplayed())).
                perform(typeText(amount), closeSoftKeyboard());
        onView(withId(R.id.add_expense_description)).check(matches(isDisplayed())).
                perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.add_expense_currency)).check(matches(isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(currency))).
                inRoot(isPlatformPopup()).perform(click());
        rotateScreen();
        //then
        onView(withId(R.id.add_expense_dialog_from)).
                check(matches(withSpinnerText(containsString(USERS.get(0)))));
        onView(withId(R.id.add_expense_amount)).check(matches(withText(amount)));
        onView(withId(R.id.add_expense_description)).check(matches(withText(description)));
        onView(withId(R.id.add_expense_currency)).
                check(matches(withSpinnerText(containsString(currency))));
    }
}

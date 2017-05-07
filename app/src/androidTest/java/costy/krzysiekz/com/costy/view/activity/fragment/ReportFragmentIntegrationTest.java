package costy.krzysiekz.com.costy.view.activity.fragment;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
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
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;
import java8.util.stream.StreamSupport;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.addProjectAndClickOnIt;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.clickNavigationDrawerItem;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.createExpense;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReportFragmentIntegrationTest {

    private static final List<String> USERS = Arrays.asList("User 1", "User 2", "User 3");
    private static final String PROJECT_TEST_NAMING = "Report Fragment Test {0}";
    private static final String DEFAULT_CURRENCY = "PLN";

    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    private static AtomicInteger testCounter = new AtomicInteger(0);
    private IdlingResource idlingResource;
    private String projectName;

    @Before
    public void setUp() throws Exception {
        projectName = MessageFormat.format(PROJECT_TEST_NAMING, testCounter.incrementAndGet());
        addProjectAndClickOnIt(projectName, DEFAULT_CURRENCY);
        StreamSupport.stream(USERS).forEach(IntegrationTestUtils::createUser);
        clickNavigationDrawerItem(R.id.nav_expenses);
        //wait a bit before interacting with fragment since there is a timing issue here
        idlingResource = new ElapsedTimeIdlingResource(200);
        Espresso.registerIdlingResources(idlingResource);
    }

    @After
    public void tearDown() throws Exception {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void shouldShowEmptyReportWhenAllUsersPayTheSame() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        createExpense(USERS.get(1), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        createExpense(USERS.get(2), amount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        clickNavigationDrawerItem(R.id.nav_report);
        //then
        onView(withId(R.id.report_entries_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void shouldShowEmptyReportWhenUserPaysOnlyForHimself() {
        //given
        String amount = "10";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), amount, description, Arrays.asList(USERS.get(1), USERS.get(2)),
                DEFAULT_CURRENCY);
        clickNavigationDrawerItem(R.id.nav_report);
        //then
        onView(withId(R.id.report_entries_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void shouldShowProperReportForSingleCurrency() {
        //given
        String firstExpenseAmount = "15";
        String secondExpenseAmount = "5";
        String description = "Sample description";
        //when
        createExpense(USERS.get(0), firstExpenseAmount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        createExpense(USERS.get(1), secondExpenseAmount, description, Arrays.asList(USERS.get(1),
                USERS.get(2)), DEFAULT_CURRENCY);
        clickNavigationDrawerItem(R.id.nav_report);
        //then
        onView(withId(R.id.report_entries_recycler_view)).check(new RecyclerViewItemCountAssertion(1));
        onView(withId(R.id.report_entries_recycler_view)).
                check(matches(hasDescendant(withText("5.000 PLN"))));
    }

    @Test
    public void shouldShowProperReportForMultipleCurrenciesCurrency() {
        //given
        String firstExpenseAmount = "15";
        String secondExpenseAmount = "5";
        String thirdExpenseAmount = "30";
        String fourthExpenseAmount = "10";
        String description = "Sample description";
        String secondCurrency = "EUR";
        //when
        createExpense(USERS.get(0), firstExpenseAmount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        createExpense(USERS.get(1), secondExpenseAmount, description, Arrays.asList(USERS.get(1),
                USERS.get(2)), DEFAULT_CURRENCY);
        createExpense(USERS.get(0), thirdExpenseAmount, description, Collections.emptyList(), secondCurrency);
        createExpense(USERS.get(1), fourthExpenseAmount, description, Arrays.asList(USERS.get(1),
                USERS.get(2)), secondCurrency);
        clickNavigationDrawerItem(R.id.nav_report);
        //then
        onView(withId(R.id.report_entries_recycler_view)).check(new RecyclerViewItemCountAssertion(2));
        onView(withId(R.id.report_entries_recycler_view)).
                check(matches(hasDescendant(withText("5.000 PLN"))));
        onView(withId(R.id.report_entries_recycler_view)).
                check(matches(hasDescendant(withText("10.000 EUR"))));
    }

    @Test
    public void shouldShareReport() throws InterruptedException {
        //given
        String firstExpenseAmount = "15";
        String secondExpenseAmount = "5";
        String description = "Sample description";
        String expectedReport = "Project: " + projectName +
                "\n\n" +
                "Expenses:\n" +
                "Sample description: 15 PLN\n" +
                "User 1 -> [User 1, User 2, User 3]\n" +
                "\n" +
                "Sample description: 5 PLN\n" +
                "User 2 -> [User 1]\n" +
                "\n" +
                "Report:\n" +
                "User 3 -> User 1: 5 PLN";
        //when
        createExpense(USERS.get(0), firstExpenseAmount, description, Collections.emptyList(), DEFAULT_CURRENCY);
        createExpense(USERS.get(1), secondExpenseAmount, description, Arrays.asList(USERS.get(1),
                USERS.get(2)), DEFAULT_CURRENCY);
        clickNavigationDrawerItem(R.id.nav_report);
        Thread.sleep(500);
        onView(withId(R.id.share_report_button)).check(matches(isDisplayed())).perform(click());
        //then
        intended(allOf(hasAction(Intent.ACTION_CHOOSER),
                hasExtra(is(Intent.EXTRA_INTENT),
                        allOf(hasAction(Intent.ACTION_SEND),
                                hasExtra(Intent.EXTRA_TEXT, expectedReport)))));
    }
}

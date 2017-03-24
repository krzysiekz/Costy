package costy.krzysiekz.com.costy.view.activity.fragment;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.utils.RecyclerViewItemCountAssertion;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.addProjectAndClickOnIt;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.createUser;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PeopleFragmentIntegrationTest {

    private static final String PROJECT_TEST_NAMING = "People Fragment Test {0}";
    private static final String DEFAULT_CURRENCY = "EUR";
    
    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    private static AtomicInteger testCounter = new AtomicInteger(0);

    @Before
    public void setUp() throws Exception {
        addProjectAndClickOnIt(MessageFormat.format(PROJECT_TEST_NAMING,
                testCounter.incrementAndGet()), DEFAULT_CURRENCY);
    }

    @Test
    public void shouldAddPersonToProjectWhenNameTyped() throws InterruptedException {
        //given
        String username = "Sample user";
        //when
        createUser(username);
        //then
        onView(withId(R.id.people_recycler_view)).
                check(matches(hasDescendant(withId(R.id.item_person_name))));
        onView(withId(R.id.people_recycler_view)).
                check(matches(hasDescendant(withText(username))));
    }

    @Test
    public void shouldShowToastWhenUsernameEmpty() {
        //when
        onView(withId(R.id.add_person_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        //then
        onView(withText(R.string.wrong_person_name))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.people_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void shouldHaveActionModeDisabledByDefault() {
        //then
        onView(withId(R.id.menu_remove)).check(doesNotExist());
    }

    @Test
    public void shouldHaveActionModeEnabledWhenItemLongClicked() {
        //given
        String username = "Sample user";
        //when
        createUser(username);
        onView(withId(R.id.people_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(username)), longClick()));
        //then
        onView(withId(R.id.menu_remove)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldRemovePersonFromProject() {
        //given
        String username = "Sample user";
        //when
        createUser(username);
        onView(withId(R.id.people_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(username)), longClick()));
        onView(withId(R.id.menu_remove)).perform(click());
        //then
        onView(withId(R.id.people_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }
}

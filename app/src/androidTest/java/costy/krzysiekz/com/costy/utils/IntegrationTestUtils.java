package costy.krzysiekz.com.costy.utils;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;

import java.util.List;

import costy.krzysiekz.com.costy.R;
import java8.util.stream.StreamSupport;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class IntegrationTestUtils {

    public static void addProjectAndClickOnIt(String projectName, String defaultCurrency) {
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
        onView(withId(R.id.add_project_dialog_default_currency)).check(matches(isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(defaultCurrency))).
                inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.projects_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(projectName)), click()));
    }

    public static void clickNavigationDrawerItem(int menuItemId) {
        onView(withId(R.id.selected_project_nav_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.selected_project_nav_drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.selected_project_nav_view)).perform(NavigationViewActions.navigateTo(menuItemId));
    }

    public static void createUser(String username) {
        onView(withId(R.id.add_person_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.person_name)).check(matches(isDisplayed())).
                perform(typeText(username), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
    }

    public static void createExpense(String payer, String amount, String description,
                                     List<String> peopleToUnselected, String currency) {
        onView(withId(R.id.add_expense_button)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.add_expense_dialog_from)).check(matches(isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(payer))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.add_expense_receivers)).check(matches(isDisplayed())).perform(click());
        StreamSupport.stream(peopleToUnselected).forEach(u -> onData(allOf(is(instanceOf(String.class)), is(u))).perform(click()));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.add_expense_amount)).check(matches(isDisplayed())).
                perform(typeText(amount), closeSoftKeyboard());
        onView(withId(R.id.add_expense_description)).check(matches(isDisplayed())).
                perform(typeText(description), closeSoftKeyboard());
        onView(withId(R.id.add_expense_currency)).check(matches(isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(currency))).
                inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }
}

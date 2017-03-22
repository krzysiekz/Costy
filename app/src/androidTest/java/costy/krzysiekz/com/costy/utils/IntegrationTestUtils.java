package costy.krzysiekz.com.costy.utils;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;

import costy.krzysiekz.com.costy.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class IntegrationTestUtils {

    public static void addProjectAndClickOnIt(String projectName) {
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
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
}

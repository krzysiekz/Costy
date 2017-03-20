package costy.krzysiekz.com.costy.view.activity;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectedProjectActivityIntegrationTest {

    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    @Test
    public void shouldShowPeopleFragment() {
        //given
        addProjectAndClickOnIt("People Fragment Nav");
        //when
        onView(withId(R.id.selected_project_nav_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.selected_project_nav_drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.selected_project_nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_people));
        //then
        onView(withId(R.id.fragment_people_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowExpensesFragment() {
        //given
        addProjectAndClickOnIt("Expenses Fragment Nav");
        //when
        onView(withId(R.id.selected_project_nav_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.selected_project_nav_drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.selected_project_nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_expenses));
        //then
        onView(withId(R.id.expenses_fragment_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowReportFragment() {
        //given
        addProjectAndClickOnIt("Report Fragment Nav");
        //when
        onView(withId(R.id.selected_project_nav_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.selected_project_nav_drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.selected_project_nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_report));
        //then
        onView(withId(R.id.report_fragment_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowSettingsFragment() {
        //given
        addProjectAndClickOnIt("Settings Fragment Nav");
        //when
        onView(withId(R.id.selected_project_nav_drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.selected_project_nav_drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.selected_project_nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        //then
        onView(withId(R.id.settings_fragment_id)).check(matches(isDisplayed()));
    }

    private void addProjectAndClickOnIt(String projectName) {
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.projects_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(projectName)), click()));
    }
}

package costy.krzysiekz.com.costy.view.activity;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import costy.krzysiekz.com.costy.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.addProjectAndClickOnIt;
import static costy.krzysiekz.com.costy.utils.IntegrationTestUtils.clickNavigationDrawerItem;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SelectedProjectActivityIntegrationTest {

    private static final String DEFAULT_CURRENCY = "EUR";

    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    @Test
    public void shouldShowPeopleFragment() {
        //given
        addProjectAndClickOnIt("People Fragment Nav", DEFAULT_CURRENCY);
        //when
        clickNavigationDrawerItem(R.id.nav_people);
        //then
        onView(withId(R.id.fragment_people_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowExpensesFragment() {
        //given
        addProjectAndClickOnIt("Expenses Fragment Nav", DEFAULT_CURRENCY);
        //when
        clickNavigationDrawerItem(R.id.nav_expenses);
        //then
        onView(withId(R.id.expenses_fragment_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowReportFragment() {
        //given
        addProjectAndClickOnIt("Report Fragment Nav", DEFAULT_CURRENCY);
        //when
        clickNavigationDrawerItem(R.id.nav_report);
        //then
        onView(withId(R.id.report_fragment_id)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowSettingsFragment() {
        //given
        addProjectAndClickOnIt("Settings Fragment Nav", DEFAULT_CURRENCY);
        //when
        clickNavigationDrawerItem(R.id.nav_settings);
        //then
        onView(withId(R.id.settings_fragment_id)).check(matches(isDisplayed()));
    }


}

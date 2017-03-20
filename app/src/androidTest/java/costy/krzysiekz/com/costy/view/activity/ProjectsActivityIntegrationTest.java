package costy.krzysiekz.com.costy.view.activity;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.utils.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProjectsActivityIntegrationTest {

    @Rule
    public IntentsTestRule<ProjectsActivity> mActivityRule =
            new IntentsTestRule<>(ProjectsActivity.class);

    @Test
    public void shouldShowPopupWhenAddingNewProject() {
        //given
        //when
        onView(withId(R.id.add_project_button)).perform(click());
        //then
        onView(withId(R.id.project_name)).inRoot(isDialog()).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).check(matches(isDisplayed()));
    }

    @Test
    public void shouldAddProjectWhenNameTyped() {
        //given
        String projectName = "Some project";
        //when
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //then
        onView(withId(R.id.projects_recycler_view)).
                check(matches(hasDescendant(withId(R.id.item_project_name))));
        onView(withId(R.id.projects_recycler_view)).
                check(matches(hasDescendant(withText(projectName))));
    }

    @Test
    public void shouldNotAddProjectWhenNameEmpty() {
        //given
        String projectName = "";
        //when
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        //then
        onView(withText(R.string.wrong_project_name))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.projects_recycler_view)).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void shouldStartSelectedProjectActivityWhenUserClicksOnProject() {
        //given
        String projectName = "Some project 2";
        //when
        onView(withId(R.id.add_project_button)).perform(click());
        onView(withId(R.id.project_name)).
                check(matches(isDisplayed())).
                perform(typeText(projectName), closeSoftKeyboard());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.projects_recycler_view)).
                perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(projectName)), click()));
        //then
        intended(hasComponent(SelectedProjectActivity.class.getName()));
        intended(hasExtra(SelectedProjectActivity.PROJECT_NAME, projectName));
    }
}
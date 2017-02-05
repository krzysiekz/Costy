package costy.krzysiekz.com.costy.view.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.krzysiekz.costy.model.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Arrays;

import costy.krzysiekz.com.costy.BuildConfig;
import costy.krzysiekz.com.costy.R;
import costy.krzysiekz.com.costy.TestCostyApplication;
import costy.krzysiekz.com.costy.model.di.PresenterModuleMock;
import costy.krzysiekz.com.costy.presenter.impl.PeoplePresenter;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.adapter.PeopleAdapter;
import costy.krzysiekz.com.costy.view.activity.dialog.AddPersonDialogFragment;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 24, application = TestCostyApplication.class)
public class PeopleFragmentTest {

    private PresenterModuleMock presenterModuleMock;
    private PeopleFragment fragment;
    private static final String PROJECT_NAME = "Some project name";

    @Before
    public void setUp() throws Exception {
        //given
        setUpModulesMocks();
        fragment = new PeopleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SelectedProjectActivity.PROJECT_NAME, PROJECT_NAME);
        fragment.setArguments(bundle);
        //when
        SupportFragmentTestUtil.startVisibleFragment(fragment);
    }

    private void setUpModulesMocks() {
        TestCostyApplication app = (TestCostyApplication) RuntimeEnvironment.application;
        presenterModuleMock = new PresenterModuleMock();
        app.setPresenterModule(presenterModuleMock);
    }

    @Test
    public void shouldInjectPresenter() {
        //then
        assertThat(fragment.presenter).isNotNull().isInstanceOf(PeoplePresenter.class);
    }

    @Test
    public void shouldAttachViewToThePresenter() {
        //then
        verify(presenterModuleMock.getPeoplePresenter()).attachView(fragment);
    }

    @Test
    public void shouldLoadPeopleUponActivityStart() {
        //then
        verify(presenterModuleMock.getPeoplePresenter()).loadProjectPeople(PROJECT_NAME);
    }

    @Test
    public void shouldPassPeopleToAdapter() {
        //given
        User kate = new User("Kate");
        User john = new User("John");
        //when
        RecyclerView recyclerView = fragment.peopleRecyclerView;
        fragment.showPeople(Arrays.asList(kate, john));
        //then
        assertThat(recyclerView).isNotNull();
        assertThat(recyclerView.getAdapter()).isInstanceOf(PeopleAdapter.class);
        assertThat(recyclerView.getAdapter().getItemCount()).isEqualTo(2);
        assertThat(((PeopleAdapter) recyclerView.getAdapter()).getPeople()).containsOnly(kate, john);
    }

    @Test
    public void shouldCallDialogFragmentAfterClickingAddPersonButton() {
        //when
        fragment.addPersonButton.performClick();
        Fragment popup = fragment.getActivity().getSupportFragmentManager().
                findFragmentByTag(AddPersonDialogFragment.TAG);
        //then
        assertThat(popup).isNotNull();
        assertThat(popup).isInstanceOf(AddPersonDialogFragment.class);
    }

    @Test
    public void shouldCallPresenterWhenPersonNameConfirmed() {
        //given
        String personName = "John";
        //when
        fragment.onPersonNameConfirmed(personName);
        //then
        verify(presenterModuleMock.getPeoplePresenter()).addPerson(PROJECT_NAME, personName);
    }

    @Test
    public void shouldShowToastWhenWrongName() {
        //given
        final Context context = RuntimeEnvironment.application;
        //when
        fragment.showWrongNameError();
        //then
        assertThat(ShadowToast.getLatestToast()).isNotNull();
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(context.getString(R.string.wrong_person_name));
    }
}
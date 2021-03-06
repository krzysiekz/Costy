package costy.krzysiekz.com.costy.model.di;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;
import costy.krzysiekz.com.costy.view.activity.SelectedProjectActivity;
import costy.krzysiekz.com.costy.view.activity.fragment.ExpensesFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.PeopleFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.ReportFragment;
import costy.krzysiekz.com.costy.view.activity.fragment.SettingsFragment;
import dagger.Component;

@Singleton
@Component(modules = {MainModule.class, PresenterModule.class})
public interface GraphComponent {

    void inject(ProjectsActivity projectsActivity);

    void inject(ExpensesFragment expensesFragment);

    void inject(SelectedProjectActivity selectedProjectActivity);

    void inject(PeopleFragment peopleFragment);

    void inject(ReportFragment reportFragment);

    void inject(SettingsFragment settingsFragment);
}

package costy.krzysiekz.com.costy.model.di;

import javax.inject.Singleton;

import costy.krzysiekz.com.costy.view.activity.ExpensesActivity;
import costy.krzysiekz.com.costy.view.activity.ProjectsActivity;
import dagger.Component;

@Singleton
@Component(modules = {MainModule.class, PresenterModule.class})
public interface GraphComponent {

    void inject(ProjectsActivity projectsActivity);

    void inject(ExpensesActivity expensesActivity);
}

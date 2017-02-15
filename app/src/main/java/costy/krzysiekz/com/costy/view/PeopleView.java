package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.User;

import java.util.List;
import java.util.Set;

public interface PeopleView extends MVPView {
    void showPeople(List<User> users);

    void removePeople(Set<Integer> positions);

    void showPeopleUsedInExpensesError();
}

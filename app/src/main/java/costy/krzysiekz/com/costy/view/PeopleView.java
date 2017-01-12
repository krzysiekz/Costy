package costy.krzysiekz.com.costy.view;

import com.krzysiekz.costy.model.User;

import java.util.List;

public interface PeopleView extends MVPView {
    void showPeople(List<User> users);
}

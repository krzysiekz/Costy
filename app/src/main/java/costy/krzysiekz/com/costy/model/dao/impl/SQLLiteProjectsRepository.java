package costy.krzysiekz.com.costy.model.dao.impl;

import com.krzysiekz.costy.model.Currency;
import com.krzysiekz.costy.model.ExpenseProject;
import com.krzysiekz.costy.model.User;
import com.krzysiekz.costy.model.UserExpense;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import costy.krzysiekz.com.costy.model.dao.ProjectsRepository;
import costy.krzysiekz.com.costy.model.dao.converter.impl.CurrencyConverter;
import costy.krzysiekz.com.costy.model.dao.converter.impl.ExpenseProjectConverter;
import costy.krzysiekz.com.costy.model.dao.converter.impl.UserConverter;
import costy.krzysiekz.com.costy.model.dao.entity.CurrencyEntity;
import costy.krzysiekz.com.costy.model.dao.entity.ExpenseProjectEntity;
import costy.krzysiekz.com.costy.model.dao.entity.UserEntity;
import costy.krzysiekz.com.costy.model.dao.entity.UserExpenseEntity;
import costy.krzysiekz.com.costy.model.dao.entity.UserExpenseRelation;
import java8.util.Optional;
import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.StreamSupport;

public class SQLLiteProjectsRepository implements ProjectsRepository {

    private CurrencyConverter currencyConverter;
    private UserConverter userConverter;
    private ExpenseProjectConverter expenseProjectConverter;

    @Override
    public void addProject(ExpenseProject project) {
        Optional<CurrencyEntity> currencyEntity = getCurrencyEntity(project.getDefaultCurrency().getName());
        currencyEntity.ifPresent(e -> {
            ExpenseProjectEntity entity = expenseProjectConverter.toEntity(project);
            entity.setDefaultCurrency(e);
            entity.save();
        });
    }

    private Optional<CurrencyEntity> getCurrencyEntity(String name) {
        List<CurrencyEntity> currencyEntities =
                CurrencyEntity.find(CurrencyEntity.class, "NAME = ?", name);
        return StreamSupport.stream(currencyEntities).findFirst();
    }

    private Optional<ExpenseProjectEntity> getExpenseProjectEntity(String name) {
        List<ExpenseProjectEntity> entities =
                ExpenseProjectEntity.find(ExpenseProjectEntity.class, "NAME = ?", name);
        return StreamSupport.stream(entities).findFirst();
    }

    private Optional<UserEntity> getUserEntity(String name) {
        List<UserEntity> entities = UserEntity.find(UserEntity.class, "NAME = ?", name);
        return StreamSupport.stream(entities).findFirst();
    }

    @Override
    public List<ExpenseProject> getAllProjects() {
        List<ExpenseProjectEntity> entities = ExpenseProjectEntity.listAll(ExpenseProjectEntity.class);
        return StreamSupport.stream(entities).
                map(e -> expenseProjectConverter.fromEntity(e)).
                collect(Collectors.toList());
    }

    @Override
    public ExpenseProject getProject(String s) {
        Optional<ExpenseProjectEntity> first = getExpenseProjectEntity(s);
        return first.map(e -> {
            ExpenseProject project = expenseProjectConverter.fromEntity(e);
            project.setDefaultCurrency(currencyConverter.fromEntity(e.getDefaultCurrency()));
            StreamSupport.stream(getAllUsers(s)).forEach(project::addUser);
            StreamSupport.stream(getAllExpenses(s)).forEach(project::addExpense);
            return project;
        }).orElse(null);
    }


    @Override
    public void updateProject(ExpenseProject project) {
        getExpenseProjectEntity(project.getName()).ifPresent(p -> {
            p.setName(project.getName());
            if (!p.getDefaultCurrency().getName().equals(project.getDefaultCurrency().getName())) {
                getCurrencyEntity(project.getDefaultCurrency().getName()).ifPresent(p::setDefaultCurrency);
            }
            p.save();
        });
    }

    @Override
    public void removeProjects(Collection<ExpenseProject> projects) {
        StreamSupport.stream(projects).forEach(p -> {
            Optional<ExpenseProjectEntity> expenseProjectEntity = getExpenseProjectEntity(p.getName());
            List<Integer> positions = IntStreams.range(0, p.getExpenses().size() - 1).boxed().
                    collect(Collectors.toList());

            expenseProjectEntity.ifPresent(pe -> {
                removeExpenses(positions, pe);
                removeUsers(p.getUsers(), pe);
                pe.delete();
            });
        });
    }

    @Override
    public List<Currency> getAllCurrencies() {
        List<CurrencyEntity> entities = CurrencyEntity.listAll(CurrencyEntity.class);
        return StreamSupport.stream(entities).map(e -> currencyConverter.fromEntity(e)).
                collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers(String projectName) {
        return getExpenseProjectEntity(projectName).
                map(ExpenseProjectEntity::getUsers).
                map(l -> StreamSupport.stream(l).
                        map(ue -> userConverter.fromEntity(ue)).
                        collect(Collectors.toList())).get();
    }

    @Override
    public List<UserExpense> getAllExpenses(String projectName) {
        return getExpenseProjectEntity(projectName).
                map(ExpenseProjectEntity::getExpenses).
                map(l -> StreamSupport.stream(l).
                        map(ee -> {
                            List<UserExpenseRelation> userExpenseRelations = UserExpenseRelation.
                                    find(UserExpenseRelation.class, "EXPENSE = ?", String.valueOf(ee.getId()));
                            List<User> receivers = StreamSupport.stream(userExpenseRelations).
                                    map(UserExpenseRelation::getUser).
                                    map(ue -> userConverter.fromEntity(ue)).collect(Collectors.toList());
                            return new UserExpense.UserExpenseBuilder().
                                    withAmount(new BigDecimal(ee.getAmount())).
                                    withDescription(ee.getDescription()).
                                    withUser(userConverter.fromEntity(ee.getUser())).
                                    withCurrency(currencyConverter.fromEntity(ee.getCurrency())).
                                    withReceivers(receivers).build();
                        }).collect(Collectors.toList())).get();
    }

    @Override
    public void addPerson(String projectName, User user) {
        getExpenseProjectEntity(projectName).ifPresent(p -> {
            UserEntity userEntity = userConverter.toEntity(user);
            userEntity.setExpenseProject(p);
            userEntity.save();
        });
    }

    @Override
    public void addExpense(String projectName, UserExpense expense) {
        getExpenseProjectEntity(projectName).ifPresent(p -> {
            Optional<UserEntity> userEntityOptional = getUserEntity(expense.getUser().getName());
            Optional<CurrencyEntity> currencyOptional = getCurrencyEntity(expense.getCurrency().getName());

            UserExpenseEntity entity = new UserExpenseEntity();
            entity.setExpenseProject(p);
            entity.setAmount(expense.getAmount().toString());
            entity.setCurrency(currencyOptional.get());
            entity.setUser(userEntityOptional.get());
            entity.setDescription(expense.getDescription());
            entity.save();

            for (User receiver : expense.getReceivers()) {
                Optional<UserEntity> r = getUserEntity(receiver.getName());
                UserExpenseRelation relation = new UserExpenseRelation();
                relation.setUser(r.get());
                relation.setExpense(entity);
                relation.save();
            }
        });
    }

    @Override
    public void removeExpenses(String projectName, Collection<Integer> positions) {
        getExpenseProjectEntity(projectName).ifPresent(p -> removeExpenses(positions, p));
    }

    private void removeExpenses(Collection<Integer> positions, ExpenseProjectEntity p) {
        List<UserExpenseEntity> expenses = p.getExpenses();
        StreamSupport.stream(positions).map(expenses::get).forEach(ee -> {
            List<UserExpenseRelation> userExpenseRelations = UserExpenseRelation.
                    find(UserExpenseRelation.class, "EXPENSE = ?", String.valueOf(ee.getId()));
            StreamSupport.stream(userExpenseRelations).forEach(r -> r.delete());
            ee.delete();
        });
    }

    @Override
    public void removeUsers(String projectName, Collection<User> usersToRemove) {
        getExpenseProjectEntity(projectName).ifPresent(p -> removeUsers(usersToRemove, p));
    }

    @Override
    public Currency getProjectDefaultCurrency(String projectName) {
        Optional<ExpenseProjectEntity> projectEntity = getExpenseProjectEntity(projectName);
        return projectEntity.map(pe -> currencyConverter.fromEntity(pe.getDefaultCurrency())).get();
    }

    @Override
    public void changeDefaultCurrency(String projectName, String newCurrency) {
        Optional<ExpenseProjectEntity> projectEntity = getExpenseProjectEntity(projectName);
        Optional<CurrencyEntity> currencyEntity = getCurrencyEntity(newCurrency);
        projectEntity.ifPresent(pe -> currencyEntity.ifPresent(ce -> {
            pe.setDefaultCurrency(ce);
            pe.save();
        }));
    }

    private void removeUsers(Collection<User> usersToRemove, ExpenseProjectEntity p) {
        List<UserEntity> users = p.getUsers();
        List<String> namesToRemove = StreamSupport.stream(usersToRemove).map(User::getName).collect(Collectors.toList());
        StreamSupport.stream(users).filter(u -> namesToRemove.contains(u.getName())).forEach(u -> u.delete());
    }

    public void setCurrencyConverter(CurrencyConverter currencyConverter) {
        this.currencyConverter = currencyConverter;
    }

    public void setUserConverter(UserConverter userConverter) {
        this.userConverter = userConverter;
    }

    public void setExpenseProjectConverter(ExpenseProjectConverter expenseProjectConverter) {
        this.expenseProjectConverter = expenseProjectConverter;
    }
}
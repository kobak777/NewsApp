package com.company.newsapp.web.screens;

import com.company.newsapp.entity.Employee;
import com.company.newsapp.entity.News;
import com.company.newsapp.entity.NewsCategory;
import com.haulmont.cuba.core.entity.annotation.PublishEntityChangedEvents;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.*;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("newsapp_Profile")
@UiDescriptor("profile.xml")
@PublishEntityChangedEvents
@DialogMode(width = "1200px", height = "500px", forceDialog = true)
public class Profile extends Screen {

    @Inject
    private CollectionLoader<News> newsDl;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceLoader<Employee> employeeDl;
    @Inject
    private Notifications notifications;
    @Inject
    private InstanceContainer<Employee> employeeDc;
    @Inject
    private DataContext dataContext;
    @Inject
    private CollectionLoader<NewsCategory> newsCategoriesDl;
    @Inject
    private Table<NewsCategory> newsCategoriesTable;
    @Inject
    private CollectionLoader<Employee> subscribersDl;
    @Inject
    private CollectionContainer<Employee> subscribersDc;
    @Inject
    private Button subscribe;
    @Inject
    private Button describe;

    private int userSubscriptionCount;
    @Inject
    private Table<News> newsTable;
    @Inject
    private ComponentsFactory componentsFactory;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private Button createBtn;
    @Inject
    private Button editBtn;
    @Inject
    private Button removeBtn;


    @Subscribe
    public void onInit(InitEvent event) {
        newsTable.addGeneratedColumn("information", entity -> {
            Label<String> label = componentsFactory.createComponent(Label.class);
            label.setValue(entity.getInformation());
            label.setStyleName("news-content-label");
            label.setWidth("300px");
            return label;
        });
        newsCategoriesTable.addGeneratedColumn("description", entity -> {
            Label<String> label = uiComponents.create(Label.TYPE_STRING);
            label.setValue(entity.getDescription());
            label.setStyleName("news-table-information");
            label.setWidth("300px");
            return label;
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        subscribe.setVisible(false);
        describe.setVisible(false);
        if(userSessionSource.getUserSession().getRoles().contains("default-user")){
            createBtn.setVisible(false);
            editBtn.setVisible(false);
            removeBtn.setVisible(false);
        }
        else{
            createBtn.setVisible(true);
            editBtn.setVisible(true);
            removeBtn.setVisible(true);
        }

        employeeDl.setParameter("session$userId", userSessionSource.getUserSession().getUser().getId());
        employeeDl.load();

        newsDl.setParameter("session$userId", userSessionSource.getUserSession().getUser().getId());
        newsDl.load();
        List<News> sortedNews = new ArrayList<>(newsDl.getContainer().getItems());
        sortedNews.sort((n1, n2) -> n2.getDate().compareTo(n1.getDate())); // от новых к старым
        newsDl.getContainer().setItems(sortedNews);
        newsCategoriesDl.load();

        updateSubscriptionStatus();
        updateUserSubscriptionCount();
        updateTableHeader();

    }

    private void updateUserSubscriptionCount() {
        User currentUser = userSessionSource.getUserSession().getUser();
        int count = 0;

        List<NewsCategory> newsCategories = newsCategoriesDl.getContainer().getItems();
        for (NewsCategory category : newsCategories) {
            for (Employee subscriber : category.getSubscribers()) {
                if (subscriber.getUser() != null && subscriber.getUser().getId().equals(currentUser.getId())) {
                    count++;
                    break;
                }
            }
        }

        userSubscriptionCount = count;
    }

    private void updateTableHeader() {
        String headerText = "Подписки (" + userSubscriptionCount + ")";
        newsCategoriesTable.setCaption(headerText);
    }

    private void updateSubscriptionStatus() {
        User currentUser = userSessionSource.getUserSession().getUser();
        List<NewsCategory> newsCategories = newsCategoriesDl.getContainer().getItems();

        for (NewsCategory category : newsCategories) {
            boolean isSubscribed = false;
            for (Employee subscriber : category.getSubscribers()) {
                if (subscriber.getUser() != null && subscriber.getUser().getId().equals(currentUser.getId())) {
                    isSubscribed = true;
                    break;
                }
            }
            category.setSubscriptionStatus(isSubscribed ? "Подписан" : "Не подписан");
        }
        sortCategories();

    }

    @Subscribe("saveBtn")
    public void onSaveBtnClick(Button.ClickEvent event) {
        if (dataContext.hasChanges()) {
            dataContext.commit();
            notifications.create()
                    .withCaption("Данные успешно сохранены")
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
        } else {
            notifications.create()
                    .withCaption("Нет изменений для сохранения")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }

    @Subscribe("subscribe")
    public void onSubscribeBtnClick(Button.ClickEvent event) {
        NewsCategory selectedCategory = newsCategoriesTable.getSingleSelected();
        if (selectedCategory != null) {
            Employee currentUser = employeeDc.getItem();
            List<Employee> subscribers = selectedCategory.getSubscribers();
            if (subscribers == null) {
                subscribers = new ArrayList<>();
                selectedCategory.setSubscribers(subscribers);
            }

            if (!subscribers.contains(currentUser)) {
                subscribers.add(currentUser);
                dataManager.commit(selectedCategory);
                notifications.create()
                        .withCaption("Вы успешно подписались на категорию")
                        .withType(Notifications.NotificationType.HUMANIZED)
                        .show();

                newsDl.load();
                newsCategoriesDl.load();
                updateSubscriptionStatus();
                updateUserSubscriptionCount();
                updateTableHeader();
                sortCategories();
                newsCategoriesTable.repaint();
                updateSubscriptionButtons(selectedCategory);
            }
        } else {
            notifications.create()
                    .withCaption("Пожалуйста, выберите категорию")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }

    @Subscribe("describe")
    public void onDescribeBtnClick(Button.ClickEvent event) {
        NewsCategory selectedCategory = newsCategoriesTable.getSingleSelected();
        if (selectedCategory != null) {
            Employee currentUser = employeeDc.getItem();
            List<Employee> subscribers = selectedCategory.getSubscribers();
            if (subscribers != null && subscribers.contains(currentUser)) {
                subscribers.remove(currentUser);
                dataManager.commit(selectedCategory);
                notifications.create()
                        .withCaption("Вы успешно отписались от категории")
                        .withType(Notifications.NotificationType.HUMANIZED)
                        .show();

                newsDl.load();
                newsCategoriesDl.load();
                updateSubscriptionStatus();
                updateUserSubscriptionCount();
                updateTableHeader();
                sortCategories();
                newsCategoriesTable.repaint();
                updateSubscriptionButtons(selectedCategory);
            }
        } else {
            notifications.create()
                    .withCaption("Пожалуйста, выберите категорию")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }

    private void updateSubscribers(NewsCategory selectedCategory) {
        if (selectedCategory != null) {
            List<Employee> subscribers = selectedCategory.getSubscribers();
            subscribersDc.setItems(subscribers != null ? subscribers : new ArrayList<>());
        } else {
            subscribersDc.setItems(new ArrayList<>());
        }
    }
    private void sortCategories() {
        List<NewsCategory> newsCategories = new ArrayList<>(newsCategoriesDl.getContainer().getItems());
        newsCategories.sort((category1, category2) -> {
            boolean isSubscribed1 = "Подписан".equals(category1.getSubscriptionStatus());
            boolean isSubscribed2 = "Подписан".equals(category2.getSubscriptionStatus());
            return Boolean.compare(isSubscribed2, isSubscribed1);
        });

        newsCategoriesDl.getContainer().setItems(newsCategories);
    }
    private void updateSubscriptionButtons(NewsCategory selectedCategory) {
        if (selectedCategory != null) {
            boolean isSubscribed = "Подписан".equals(selectedCategory.getSubscriptionStatus());
            // Если подписан, показываем кнопку "Отписаться", скрываем "Подписаться"
            subscribe.setVisible(!isSubscribed);
            describe.setVisible(isSubscribed);
        } else {
            // Если категория не выбрана, скрываем обе кнопки
            subscribe.setVisible(false);
            describe.setVisible(false);
        }
    }
    @Subscribe(id = "employeeDc", target = Target.DATA_CONTAINER)
    public void onEmployeeDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Employee> event) {
        if ("lastname".equals(event.getProperty()) ||
                "name".equals(event.getProperty()) ||
                "patronymic".equals(event.getProperty())) {

            Employee employee = employeeDc.getItem();

            String lastname = employee.getLastname();
            String name = employee.getName();
            String patronymic = employee.getPatronymic();

            if (lastname != null && name != null && patronymic != null) {
                // Фамилия И.О.
                String docname = String.format("%s %s.%s.",
                        lastname,
                        name.substring(0, 1).toUpperCase(),
                        patronymic.substring(0, 1).toUpperCase());
                employee.setDocname(docname);

                // Фамилия Имя Отчество
                String displayname = String.format("%s %s %s", lastname, name, patronymic);
                employee.setDisplayname(displayname);

                User user = dataManager.reload(employee.getUser(), View.LOCAL);
                if (user != null) {
                    user.setName(displayname);
                    user.setFirstName(name);
                    user.setLastName(lastname);
                    user.setMiddleName(patronymic);
                    dataManager.commit(user);
                }
            }
        }
    }


    @Subscribe("newsCategoriesTable")
    public void onNewsCategoriesTableSelection(Table.SelectionEvent<NewsCategory> event) {
        updateSubscribers(event.getSource().getSingleSelected());
        updateSubscriptionButtons(event.getSource().getSingleSelected());
    }

    @Install(to = "newsCategoriesTable.edit", subject = "afterCommitHandler")
    private void newsCategoriesTableEditAfterCommitHandler(NewsCategory newsCategory) {
        updateSubscriptionStatus();
    }

    @Install(to = "newsCategoriesTable.create", subject = "afterCommitHandler")
    private void newsCategoriesTableCreateAfterCommitHandler(NewsCategory newsCategory) {
        updateSubscriptionStatus();
    }
}

package com.company.newsapp.web.screens.newscategory;

//import com.company.newsapp.web.screens.SubscriptionView;

import com.company.newsapp.entity.NewsCategory;
import com.company.newsapp.web.screens.SubscriptionInfo;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;


@UiController("newsapp_NewsCategory.browse")
@UiDescriptor("news-category-browse.xml")
@LookupComponent("newsCategoriesTable")
@LoadDataBeforeShow



public class NewsCategoryBrowse extends StandardLookup<NewsCategory> {
    @Inject
    private GroupTable<NewsCategory> newsCategoriesTable;
    @Inject
    private Screens screens;
    @Inject
    private Notifications notifications;
    @Inject
    private UiComponents uiComponents;


    @Subscribe("infoBtn")
    public void onInfoBtnClick(Button.ClickEvent event) {
        NewsCategory selectedCategory = newsCategoriesTable.getSingleSelected();
        if (selectedCategory != null) {
            SubscriptionInfo screen = screens.create(SubscriptionInfo.class, OpenMode.DIALOG);
            screen.setNewsCategory(selectedCategory); // Передаем выбранную категорию
            screen.show();
        } else {
            notifications.create()
                    .withCaption("Пожалуйста, выберите категорию")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }
    @Subscribe
    public void onInit(InitEvent event) {
        newsCategoriesTable.addGeneratedColumn("description", entity -> {
            Label<String> label = uiComponents.create(Label.TYPE_STRING);
            label.setValue(entity.getDescription());
            label.setStyleName("news-table-information"); // Применение стиля
            label.setWidth("300px"); // Ограничение ширины
            return label;
        });
    }
}
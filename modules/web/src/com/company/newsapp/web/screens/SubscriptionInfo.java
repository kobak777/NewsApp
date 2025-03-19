package com.company.newsapp.web.screens;

import com.company.newsapp.entity.Employee;
import com.company.newsapp.entity.NewsCategory;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;

import javax.inject.Inject;
import java.util.UUID;

@UiController("newsapp_SubscriptionInfo")
@UiDescriptor("subscription-info.xml")
public class SubscriptionInfo extends Screen {

    @Inject
    private InstanceLoader<NewsCategory> newsCategoriesDl;

    @Inject
    private InstanceContainer<NewsCategory> newsCategoryDc;

    @Inject
    private CollectionLoader<Employee> subscribersDl;

    @Inject
    private CollectionContainer<Employee> subscribersDc;


    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        NewsCategory selectedCategory = newsCategoryDc.getItem();
        if (selectedCategory != null) {
            subscribersDl.setParameter("newsCategoryId", selectedCategory.getId());
            subscribersDl.load();
        }
    }


    public void setNewsCategory(NewsCategory newsCategory) {
        newsCategoryDc.setItem(newsCategory); // Устанавливаем текущую категорию
        subscribersDl.setParameter("newsCategoryId", UUID.fromString(newsCategory.getId().toString()));
        subscribersDl.load(); // Загружаем подписчиков
    }

}

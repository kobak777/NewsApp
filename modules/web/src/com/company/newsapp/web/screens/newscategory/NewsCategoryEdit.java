package com.company.newsapp.web.screens.newscategory;

import com.company.newsapp.entity.NewsCategory;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;

@UiController("newsapp_NewsCategory.edit")
@UiDescriptor("news-category-edit.xml")
@EditedEntityContainer("newsCategoryDc")
@LoadDataBeforeShow
public class NewsCategoryEdit extends StandardEditor<NewsCategory> {

    @Inject
    private Notifications notifications;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if (getEditedEntity().getCloseDate() != null) {
            notifications.create()
                    .withCaption("Редактирование невозможно")
                    .withDescription("Эта категория завершена и не подлежит редактированию.")
                    .withType(Notifications.NotificationType.WARNING)
                    .show();
            close(WINDOW_CLOSE_ACTION);
        }
    }
}


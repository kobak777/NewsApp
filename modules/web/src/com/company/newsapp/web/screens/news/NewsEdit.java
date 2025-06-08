package com.company.newsapp.web.screens.news;

import com.haulmont.cuba.gui.screen.*;
import com.company.newsapp.entity.News;
import com.haulmont.cuba.core.global.DataManager;

import javax.inject.Inject;

@UiController("newsapp_News.edit")
@UiDescriptor("news-edit.xml")
@EditedEntityContainer("newsDc")
@LoadDataBeforeShow
public class NewsEdit extends StandardEditor<News> {

    @Inject
    private DataManager dataManager;

    @Subscribe
    protected void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        News news = getEditedEntity();

        if (news.getNumber() == null || news.getNumber() == 0) {
            Integer maxNumber = dataManager.loadValue(
                            "select max(e.number) from newsapp_News e where e.number is not null", Integer.class)
                    .softDeletion(false)
                    .one();

            news.setNumber((maxNumber != null ? maxNumber : 0) + 1);
        }
    }

}

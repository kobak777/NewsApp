package com.company.newsapp.web.screens.news;

import com.company.newsapp.entity.News;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@UiController("newsapp_News.browse")
@UiDescriptor("news-browse.xml")
@LookupComponent("newsTable")
@LoadDataBeforeShow
public class NewsBrowse extends StandardLookup<News> {

    @Inject
    private DataManager dataManager;

    @Inject
    private CollectionLoader<News> newsCollectionDl;

    @Inject
    private CollectionContainer<News> newsCollectionDc;
    @Inject
    private GroupTable<News> newsTable;

    @Inject
    private UiComponents uiComponents;

    @Subscribe
    public void onInit(InitEvent event) {
        newsTable.addGeneratedColumn("information", entity -> {
            Label<String> label = uiComponents.create(Label.TYPE_STRING);
            label.setValue(entity.getInformation());
            label.setStyleName("news-table-information");
            label.setWidth("300px");
            return label;
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        sortNewsByDescendingOrder();
    }
    private void sortNewsByDescendingOrder() {
        List<News> sortedList = newsCollectionDc.getItems().stream()
                .sorted((n1, n2) -> {
                    if (n1.getNumber() == null) return 1;
                    if (n2.getNumber() == null) return -1;
                    return n2.getNumber().compareTo(n1.getNumber());
                })
                .collect(Collectors.toList());

        newsCollectionDc.setItems(sortedList);
    }
}

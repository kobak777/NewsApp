package com.company.newsapp.web.screens.newscategory;

import com.company.newsapp.entity.Employee;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.company.newsapp.entity.NewsCategory;

import javax.inject.Inject;
import java.util.Map;

@UiController("newsapp_NewsCategory.edit")
@UiDescriptor("news-category-edit.xml")
@EditedEntityContainer("newsCategoryDc")
@LoadDataBeforeShow
public class NewsCategoryEdit extends StandardEditor<NewsCategory> {

}

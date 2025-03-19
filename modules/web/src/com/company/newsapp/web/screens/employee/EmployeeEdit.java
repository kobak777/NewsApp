package com.company.newsapp.web.screens.employee;

import com.haulmont.cuba.gui.screen.*;
import com.company.newsapp.entity.Employee;

@UiController("newsapp_Employee.edit")
@UiDescriptor("employee-edit.xml")
@EditedEntityContainer("employeeDc")
@LoadDataBeforeShow
public class EmployeeEdit extends StandardEditor<Employee> {

}
package com.company.newsapp.web.screens.employee;

import com.haulmont.cuba.gui.screen.*;
import com.company.newsapp.entity.Employee;

@UiController("newsapp_Employee.browse")
@UiDescriptor("employee-browse.xml")
@LookupComponent("employeesTable")
@LoadDataBeforeShow
public class EmployeeBrowse extends StandardLookup<Employee> {
}
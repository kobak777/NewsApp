package com.company.newsapp.web.screens.employee;

import com.company.newsapp.entity.Employee;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;

@UiController("newsapp_Employee.edit")
@UiDescriptor("employee-edit.xml")
@EditedEntityContainer("employeeDc")
@LoadDataBeforeShow
public class EmployeeEdit extends StandardEditor<Employee> {

    @Inject
    private InstanceContainer<Employee> employeeDc;

    @Inject
    private DataManager dataManager;

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
                String docname = String.format("%s %s.%s.",
                        lastname, name.charAt(0), patronymic.charAt(0));
                String displayname = String.format("%s %s %s", lastname, name, patronymic);

                employee.setDocname(docname);
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


}

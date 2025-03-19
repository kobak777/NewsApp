package com.company.newsapp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "NEWSAPP_EMPLOYEE")
@Entity(name = "newsapp_Employee")
@NamePattern("%s %s %s|lastname,name,patronymic")
public class Employee extends StandardEntity {
    private static final long serialVersionUID = -3377944203949460745L;

    @Column(name = "LASTNAME", nullable = false)
    @NotNull
    private String lastname;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "PATRONYMIC", nullable = false)
    @NotNull
    private String patronymic;

    @Column(name = "DOCNAME", nullable = false)
    @NotNull
    private String docname;

    @Column(name = "DISPLAYNAME", nullable = false)
    @NotNull
    private String displayname;

    @NotNull
    @Column(name = "EMAIL", nullable = false)
    @Email
    private String email;

    @Column(name = "PHONE", nullable = false)
    @NotNull
    private String phone;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany
    @JoinTable(name = "NEWSAPP_NEWS_CATEGORY_EMPLOYEE_LINK",
            joinColumns = @JoinColumn(name = "EMPLOYEE_ID"),
            inverseJoinColumns = @JoinColumn(name = "NEWS_CATEGORY_ID"))
    private List<NewsCategory> subscribedCategories;

    public List<NewsCategory> getSubscribedCategories() {
        return subscribedCategories;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSubscribedCategories(List<NewsCategory> subscribedCategories) {
        this.subscribedCategories = subscribedCategories;
    }
}

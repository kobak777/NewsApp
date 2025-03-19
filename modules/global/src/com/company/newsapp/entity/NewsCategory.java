package com.company.newsapp.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Table(name = "NEWSAPP_NEWS_CATEGORY")
@Entity(name = "newsapp_NewsCategory")
@NamePattern("%s|name")
public class NewsCategory extends StandardEntity {
    private static final long serialVersionUID = 1021214889787368196L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    private Employee author;

    @NotNull
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull
    @Column(name = "REG_DATE", nullable = false)
    private LocalDate regDate;

    @Column(name = "CLOSE_DATE")
    private LocalDate closeDate;

    @JoinTable(name = "NEWSAPP_NEWS_CATEGORY_EMPLOYEE_LINK",
            joinColumns = @JoinColumn(name = "NEWS_CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID"))
    @ManyToMany
    private List<Employee> subscribers;

    @OneToMany(mappedBy = "category")
    private List<News> newsList; // Новый список новостей в категории

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
    @Transient
    @MetaProperty
    private String subscriptionStatus;

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public LocalDate getCloseDate() {return closeDate;}

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Employee> subscribers) {
        this.subscribers = subscribers;
    }
}
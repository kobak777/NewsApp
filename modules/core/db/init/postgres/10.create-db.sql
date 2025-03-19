-- begin NEWSAPP_EMPLOYEE
create table NEWSAPP_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LASTNAME varchar(255) not null,
    NAME varchar(255) not null,
    PATRONYMIC varchar(255) not null,
    DOCNAME varchar(255) not null,
    DISPLAYNAME varchar(255) not null,
    EMAIL varchar(255) not null,
    PHONE varchar(255) not null,
    USER_ID uuid,
    --
    primary key (ID)
)^
-- end NEWSAPP_EMPLOYEE
-- begin NEWSAPP_NEWS_CATEGORY
create table NEWSAPP_NEWS_CATEGORY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    AUTHOR_ID uuid,
    DESCRIPTION varchar(255) not null,
    REG_DATE date not null,
    CLOSE_DATE date,
    --
    primary key (ID)
)^
-- end NEWSAPP_NEWS_CATEGORY
-- begin NEWSAPP_NEWS
create table NEWSAPP_NEWS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NUMBER_ integer not null,
    TITLE varchar(255) not null,
    INFORMATION varchar(10000) not null,
    DATE_ timestamp not null,
    AUTHOR_ID uuid not null,
    CATEGORY_ID uuid not null,
    --
    primary key (ID)
)^
-- end NEWSAPP_NEWS
-- begin NEWSAPP_NEWS_CATEGORY_EMPLOYEE_LINK
create table NEWSAPP_NEWS_CATEGORY_EMPLOYEE_LINK (
    NEWS_CATEGORY_ID uuid,
    EMPLOYEE_ID uuid,
    primary key (NEWS_CATEGORY_ID, EMPLOYEE_ID)
)^
-- end NEWSAPP_NEWS_CATEGORY_EMPLOYEE_LINK

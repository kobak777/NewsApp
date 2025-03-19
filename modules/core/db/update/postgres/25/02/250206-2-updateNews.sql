-- update NEWSAPP_NEWS set CATEGORY_ID = <default_value> where CATEGORY_ID is null ;
alter table NEWSAPP_NEWS alter column CATEGORY_ID set not null ;
